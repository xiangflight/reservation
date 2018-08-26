package com.lulu.reservation.util;

import org.springframework.util.DigestUtils;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2018/8/18 下午1:12
 * <p>
 * 类说明：
 *     签名工具类
 */

public class SignatureUtil {

    public static String getSign(String string, String key) {
        try {
            String stringSignTemp = string.concat("&key=").concat(key);
            String sign = DigestUtils.md5DigestAsHex(stringSignTemp.getBytes("UTF-8")).toUpperCase();
            return sign;
        } catch (Exception e) {
            return null;
        }
    }
}
