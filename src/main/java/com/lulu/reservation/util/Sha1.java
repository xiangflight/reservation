/*
 * 对公众平台发送给公众账号的消息加解密示例代码.
 * 
 * @copyright Copyright (c) 1998-2014 Tencent Inc.
 */

// ------------------------------------------------------------------------

package com.lulu.reservation.util;

import com.lulu.reservation.comm.exception.AesException;

import java.security.MessageDigest;
import java.util.Arrays;

/**
 * Sha1 class
 *
 * 计算公众平台的消息签名接口.
 */
public class Sha1 {

	/**
	 * 用SHA1算法生成安全签名
	 * @param token 票据
	 * @param timestamp 时间戳
	 * @param nonce 随机字符串
	 * @return 安全签名
	 * @throws AesException Aes异常
	 */
	public static String getSHA1(String token, String timestamp, String nonce, String encrypt) throws AesException {
        try {
            String[] array = new String[] { token, timestamp, nonce, encrypt};
            StringBuilder sb = new StringBuilder();
            // 字符串排序
            Arrays.sort(array);
            for (String ele: array) {
                sb.append(ele);
            }
            String str = sb.toString();
            // SHA1签名生成
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(str.getBytes());
            byte[] digest = md.digest();
            StringBuilder hexstr = new StringBuilder();
            String shaHex;
            for (Byte byteEle: digest) {
                shaHex = Integer.toHexString(byteEle & 0xFF);
                if (shaHex.length() < 2) {
                    hexstr.append(0);
                }
                hexstr.append(shaHex);
            }
            return hexstr.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AesException(AesException.ComputeSignatureError);
        }
	}

    /**
     * test sha1 method
     * @param args arguments
     */
    public static void main(String[] args) {
        try {
            final String sha1 = getSHA1("a", "b", "c", "");
            System.out.println(sha1);
        } catch (AesException e) {
            e.printStackTrace();
        }
    }
}
