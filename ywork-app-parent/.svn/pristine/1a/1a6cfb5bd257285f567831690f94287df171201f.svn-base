/**
 * 对公众平台发送给公众账号的消息加解密示例代码.
 * 
 * @copyright Copyright (c) 1998-2014 Tencent Inc.
 */

// ------------------------------------------------------------------------

/**
 * 针对org.apache.commons.codec.binary.Base64，
 * 需要导入架包commons-codec-1.9（或commons-codec-1.8等其他版本）
 * 官方下载地址：http://commons.apache.org/proper/commons-codec/download_codec.cgi
 */
package me.ywork.ticket.util;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Prpcrypt class
 *
 * 提供接收和推送给公众平台消息的加解密接口.
 */
public class Prpcrypt {

	private static Logger logger = LoggerFactory.getLogger(Prpcrypt.class);

	Charset CHARSET = StandardCharsets.UTF_8;
	byte[] aesKey;
	Base64 base64 = new Base64();

	Prpcrypt(String encodingAesKey) throws Exception {
		encodingAesKey = encodingAesKey + "=";
		aesKey = base64.decodeBase64(encodingAesKey);
	}

	// 生成4个字节的网络字节序
	byte[] getNetworkBytesOrder(int sourceNumber) {
		byte[] orderBytes = new byte[4];
		orderBytes[3] = (byte) (sourceNumber & 0xFF);
		orderBytes[2] = (byte) (sourceNumber >> 8 & 0xFF);
		orderBytes[1] = (byte) (sourceNumber >> 16 & 0xFF);
		orderBytes[0] = (byte) (sourceNumber >> 24 & 0xFF);
		return orderBytes;
	}

	// 还原4个字节的网络字节序
	int recoverNetworkBytesOrder(byte[] orderBytes) {
		int sourceNumber = 0;
		for (int i = 0; i < 4; i++) {
			sourceNumber <<= 8;
			sourceNumber |= orderBytes[i] & 0xff;
		}
		return sourceNumber;
	}

	// 随机生成16位字符串
	String getRandomStr() {
		String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 16; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 对明文进行加密
	 * @param text 需要加密的明文
	 * @param appid appid
	 * @return 加密后base64编码的字符串
	 */
	public Result encrypt(String text, String appid) {
		ByteGroup byteCollector = new ByteGroup();
		byte[] randomStrBytes = getRandomStr().getBytes(CHARSET);
		byte[] textBytes = text.getBytes(CHARSET);
		byte[] networkBytesOrder = getNetworkBytesOrder(textBytes.length);
		byte[] appidBytes = appid.getBytes(CHARSET);

		// randomStr + networkBytesOrder + text + appid
		byteCollector.addBytes(randomStrBytes);
		byteCollector.addBytes(networkBytesOrder);
		byteCollector.addBytes(textBytes);
		byteCollector.addBytes(appidBytes);

		// ... + pad: 使用自定义的填充方式对明文进行补位填充
		byte[] padBytes = PKCS7Encoder.encode(byteCollector.size());
		byteCollector.addBytes(padBytes);

		// 获得最终的字节流, 未加密
		byte[] unencrypted = byteCollector.toBytes();
		System.out.println("加密之前: " + base64.encodeToString(unencrypted));

		try {
			// 设置加密模式为AES的CBC模式
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
			IvParameterSpec iv = new IvParameterSpec(aesKey, 0, 16);
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

			// 加密
			byte[] encrypted = cipher.doFinal(unencrypted);

			// 使用BASE64对加密后的字符串进行编码
			String base64Encrypted = base64.encodeToString(encrypted);

			return new Result(0, base64Encrypted);
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(-40006, "");
		}
	}

	/**
	 * 对密文进行解密
	 * @param text 需要解密的密文
	 * @param appid appid
	 * @return 解密得到的明文
	 */
	public Result decrypt(String text, String appid) {
		byte[] original;
		try {
			// 设置解密模式为AES的CBC模式
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			SecretKeySpec key_spec = new SecretKeySpec(aesKey, "AES");
			IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
			cipher.init(Cipher.DECRYPT_MODE, key_spec, iv);

			// 使用BASE64对密文进行解码
			byte[] encrypted = Base64.decodeBase64(text);
			// 解密
			original = cipher.doFinal(encrypted);
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(-40007, "");
		}

		String xmlContent, from_appid;
		try {
			// 去除补位字符
			byte[] bytes = PKCS7Encoder.decode2(original);

			// 分离16位随机字符串,网络字节序和AppId
			byte[] networkOrder = Arrays.copyOfRange(bytes, 16, 20);

			int xmlLength = recoverNetworkBytesOrder(networkOrder);

			xmlContent = new String(Arrays.copyOfRange(bytes, 20, 20 + xmlLength), CHARSET);
			from_appid = new String(Arrays.copyOfRange(bytes, 20 + xmlLength, bytes.length),
					CHARSET);
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(-40008, "");
		}
		//System.out.println("xmlContent.xmlContent=573="+xmlContent);
		// appid不相同的情况
		if (!from_appid.equals(appid)) {
			logger.debug("密文使用的套件/应用ID={}，加密构造使用套件/应用={}", from_appid, appid);
			return new Result(-40005, "");
		}
		return new Result(0, xmlContent);

	}

	/**
	 * 回复消息给公众平台 1. 对要发送的消息进行AES-CBC加密； 2. 生成安全签名； 3. 将消息密文和安全签名打包成xml格式。
	 * 
	 * @param text 第三方需要发送的明文
	 * @return 返回码和第三方发给公众平台的xml消息
	 */
	public static Result toTencent(String text, String encodingAesKey, String nonce, String token,
			String appid, String timestamp) throws Exception {
		Prpcrypt pc = new Prpcrypt(encodingAesKey);

		// 加密
		Result encrypt = pc.encrypt(text, appid);
		if (encrypt.getCode() != 0) {
			return new Result(encrypt.getCode(), encrypt.getResult());
		}

		// 生成安全签名
		if (timestamp == "") {
			timestamp = Long.toString(System.currentTimeMillis());
		}

		Object[] signature = SHA1.getSHA1(token, timestamp, nonce, encrypt.getResult());
		if ((Integer) signature[0] != 0) {
			return new Result(encrypt.getCode(), "");
		}

		// System.out.println("发送给平台的签名是: " + signature[1].toString());
		// 生成发送的xml
		String result = XMLParse.generate(encrypt.getResult(), signature[1].toString(), timestamp,
				nonce);
		return new Result(0, result);
	}

	/**
	 * 公众平台发送消息给第三方 1. 利用收到的密文生成安全签名,进行签名验证; 2. 若验证通过，则提取xml中的加密消息； 3. 对消息进行解密。
	 * 
	 * @param text 第三方收到的xml格式加密消息
	 * @param msg_sign 第三方收到的签名
	 */
	public static Result fromTencent(String text, String encodingAesKey, String msgSignature,
			String token, String timestamp, String nonce, String appid) throws Exception {

		if (encodingAesKey.length() != 43) {
			return new Result(-40004, "");
		}

		// 密钥，公众账号的app secret
		Prpcrypt pc = new Prpcrypt(encodingAesKey);
		// 提取密文
		Object[] encrypt = XMLParse.extract(text);
		if ((Integer) encrypt[0] != 0) {
			return new Result((Integer) encrypt[0], "");
		}

		// 验证安全签名
		Object[] signature = SHA1.getSHA1(token, timestamp, nonce, encrypt[1].toString());
		if ((Integer) signature[0] != 0) {
			return new Result((Integer) signature[0], "");
		}

		// 和URL中的签名比较是否相等
		// System.out.println("第三方收到URL中的签名：" + msg_sign);
		// System.out.println("第三方校验签名：" + signature);
		if (!signature[1].equals(msgSignature)) {
			// System.out.println("签名错误 ");
			/* 不安全消息处理 */
			signature[0] = -40001;
			signature[1] = "";
			return new Result((Integer) signature[0], "");
		}
		// 解密
		Result result = pc.decrypt(encrypt[1].toString(), appid);
		return result;
	}
	

	/**
	 * 配置URL时的效验
	 * 
	 * @param text 第三方收到的xml格式加密消息
	 * @param msg_sign 第三方收到的签名
	 */
	public static Result checkURL(String text, String encodingAesKey, String msgSignature,
			String token, String timestamp, String nonce, String appid) throws Exception {
		
		if (encodingAesKey.length() != 43) {
			return new Result(-40004, "");
		}
		
		// 密钥，公众账号的app secret
		Prpcrypt pc = new Prpcrypt(encodingAesKey);

		// 验证安全签名
		Object[] signature = SHA1.getSHA1(token, timestamp, nonce, text);
		if ((Integer) signature[0] != 0) {
			return new Result((Integer) signature[0], "");
		}
		
		// 和URL中的签名比较是否相等
		// System.out.println("第三方收到URL中的签名：" + msg_sign);
		// System.out.println("第三方校验签名：" + signature);
		if (!signature[1].equals(msgSignature)) {
			// System.out.println("签名错误 ");
			/* 不安全消息处理 */
			signature[0] = -40001;
			signature[1] = "";
			return new Result((Integer) signature[0], "");
		}
	
		// 解密
		Result result = pc.decrypt(text, appid);
		return result;
	}
}