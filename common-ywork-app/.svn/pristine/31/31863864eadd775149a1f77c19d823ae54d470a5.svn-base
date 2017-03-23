package me.ywork.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.encoders.Hex;

public class AESUtil {
	private final static String encoding = "UTF-8";
	private final static byte[] iv = { 0x38, 0x37, 0x36, 0x30, 0x34, 0x33,
			0x32, 0x37, 0x38, 0x37, 0x36, 0x35, 0x31, 0x33, 0x32, 0x31 };

	/**
	 * 
	 * @param content
	 *            需要加密的内容
	 * @return 密文
	 * @throws Exception
	 */
	public static String encrypt(String content, String theKey)
			throws Exception {
		byte[] sendBytes = null;
		try {
			sendBytes = theKey.getBytes(encoding);
			content = URLEncoder.encode(content, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		BufferedBlockCipher engine = new PaddedBufferedBlockCipher(
				new CBCBlockCipher(new AESFastEngine()));
		engine.init(true, new ParametersWithIV(new KeyParameter(sendBytes), iv));
		byte[] enc = new byte[engine.getOutputSize(content.getBytes().length)];
		int size1 = engine.processBytes(content.getBytes(), 0,
				content.getBytes().length, enc, 0);
		int size2 = engine.doFinal(enc, size1);
		byte[] encryptedContent = new byte[size1 + size2];
		System.arraycopy(enc, 0, encryptedContent, 0, encryptedContent.length);
		String key = new String(Hex.encode(encryptedContent));
		return key;
	}
	
	

	/**
	 * 
	 * @param Key
	 *            密文
	 * @return 明文
	 * @throws Exception
	 */
	public static String decrypt(String Key, String theKey) throws Exception {
		byte[] sendBytes = null;
		try {
			sendBytes = theKey.getBytes(encoding);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		byte[] encryptedContent = hex2byte(Key);
		BufferedBlockCipher engine = new PaddedBufferedBlockCipher(
				new CBCBlockCipher(new AESFastEngine()));

		engine.init(false,
				new ParametersWithIV(new KeyParameter(sendBytes), iv));
		byte[] dec = new byte[engine.getOutputSize(encryptedContent.length)];
		int size1 = engine.processBytes(encryptedContent, 0,
				encryptedContent.length, dec, 0);
		int size2 = engine.doFinal(dec, size1);
		byte[] decryptedContent = new byte[size1 + size2];
		System.arraycopy(dec, 0, decryptedContent, 0, decryptedContent.length);
		String content = new String(new String(decryptedContent));
		content = URLDecoder.decode(content, "utf-8");
		return content;
	}

	public static byte[] hex2byte(String strhex) {
		if (strhex == null) {
			return null;
		}
		int l = strhex.length();
		if (l % 2 == 1) {
			return null;
		}
		byte[] b = new byte[l / 2];
		for (int i = 0; i != l / 2; i++) {
			b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2),
					16);
		}
		return b;
	}

	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}

	public static void main(String[] args) {

		String content = "B采购【2016】第007号关于车吉、拓蓝普门店积压库存处罚通报-20B二手车【2016】第016号关于门店推荐二手车商合作的通报表扬(II)B客服【2016】第194号关于11月份门店销售VIP卡专项任务的奖罚通报-200B商务【2016】第061号关于2016年10月份保险续保奖罚的通报-160B人资【2016】第105号明星通报(第五期)-50B客服【2017】第004号关于12月服务质量通报2016.12月冲红罚款-10";
		String encryptStr = null;
		try {
			encryptStr = encrypt(content, "3002bc0359b51141294323dc9d0e96b7");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("加密后=" + encryptStr);

		String decryptStr = null;
		try {
			decryptStr = decrypt(encryptStr, "3002bc0359b51141294323dc9d0e96b7");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("解密后=" + decryptStr);
	}

	public static void main2(String[] args) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH:mm");
		String key = "alidLR" + sdf.format(new Date());
		System.out.println(AESUtil.encrypt(key,
				"c5a1149f163dd7a072e235ccc2566c98"));
	}
}
