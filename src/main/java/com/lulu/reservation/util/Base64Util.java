package com.lulu.reservation.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/7/7 下午23:25
 * <p>
 *     Base64工具类
 *
 */
public class Base64Util {
    /**
     * 编码成Base64字符串
     * @param str 原字符串
     * @return Base64字符串
     */
	public static String encode2Str(String str) {
		byte[] b = null;
		String s = null;
		try {
			b = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (b != null) {
			s = new BASE64Encoder().encode(b);
		}
		return s;
	}

    /**
     * 解码成原始字符串
     * @param s Base64字符串
     * @return 原始字符串
     */
	 private static String decode2Str(String s) {
		byte[] b;
		String result = null;
		if (s != null) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				b = decoder.decodeBuffer(s);
				result = new String(b, "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}