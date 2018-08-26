package com.lulu.reservation.util;

import java.security.SecureRandom;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2018/8/18 下午12:40
 * <p>
 * 类说明：
 *     随机字符串生成类
 */

public class AlphabetRandom {

    public static String random(int length) {
        String string = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            stringBuilder.append(string.charAt(secureRandom.nextInt(string.length())));
        }
        return stringBuilder.toString();
    }

}
