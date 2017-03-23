/**
 * 对公众平台发送给公众账号的消息加解密示例代码.
 * 
 * @copyright Copyright (c) 1998-2014 Tencent Inc.
 */

// ------------------------------------------------------------------------

package me.ywork.ticket.util;

import java.nio.charset.Charset;
import java.util.Arrays;


/**
 * PKCS7Encoder class
 * 
 * 提供基于PKCS7算法的加解密接口.
 */
public class PKCS7Encoder {
	static Charset CHARSET = StandardCharsets.UTF_8;
	public static int block_size = 32;

	/**
	 * 对需要加密的明文进行填充补位
	 * 
	 * @param text 需要进行填充补位操作的明文
	 * @return 补齐明文字符串
	 */
	static byte[] encode(int text_length) {
		// 计算需要填充的位数
		int amount_to_pad = block_size - (text_length % block_size);
		if (amount_to_pad == 0) {
			amount_to_pad = block_size;
		}
		// 获得补位所用的字符
		char pad_chr = chr(amount_to_pad);
		String tmp = new String();
		for (int index = 0; index < amount_to_pad; index++) {
			tmp += pad_chr;
		}
		return tmp.getBytes(CHARSET);
	}

	/**
	 * 删除解密后明文的补位字符
	 * 
	 * @param decrypted 解密后的明文
	 * @return 删除补位字符后的明文
	 */
	static String decode(byte[] decrypted) {

		int pad = (int) decrypted[decrypted.length - 1];
		if (pad < 1 || pad > 32)
			pad = 0;
		String plain_text = new String(decrypted);
		plain_text = plain_text.substring(0, plain_text.length() - pad);
		return plain_text;
	}

	static byte[] decode2(byte[] decrypted) {
		int pad = (int) decrypted[decrypted.length - 1];
		if (pad < 1 || pad > 32) {
			pad = 0;
		}
		return Arrays.copyOfRange(decrypted, 0, decrypted.length - pad);
	}

	/**
	 * 将数字转化成ASCII码对应的字符，用于对明文进行补码
	 * 
	 * @param a 需要转化的数字
	 * @return 转化得到的字符
	 */
	static char chr(int a) {

		byte target = (byte) (a & 0xFF);
		return (char) target;
	}

}
