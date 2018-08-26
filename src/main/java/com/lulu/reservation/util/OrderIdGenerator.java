package com.lulu.reservation.util;

import org.apache.commons.lang.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2018/8/18 下午12:08
 * <p>
 * 类说明：
 *     订单号生成器
 */

public class OrderIdGenerator {

    private static final int BIT_EIGHT = 8;
    private static final int BIT_FOUR = 4;
    private static final int BIT_THREE = 3;
    private static final String FORMAT_USER_ID = "%04d";

    /**
     * 返回订单号，必要时加一层缓存解决锁表问题
     * 生成规则：
     *
     *   订单号 = 订单类型（1）+ 时间信息4位(4) + UNIX时间戳后8位(8）+  + 用户id后4位（4） + 随机数3位（3）, 20位
     *
     * @param orderType 订单类型 0：守约金
     * @return 订单号
     */
    public static String getOrderNo(int orderType, String userId) {
        String timeInfo = new SimpleDateFormat("MMdd").format(new Date());
        String timestamp = String.valueOf(System.currentTimeMillis());
        int len = timestamp.length();
        String timeStampLast8bit = timestamp.substring(len - BIT_EIGHT, len);
        String randomCode = RandomStringUtils.random(BIT_THREE, false, true);
        String userId4Bit = getUserId(userId);
        return orderType + timeInfo + timeStampLast8bit + userId4Bit + randomCode;
    }

    /**
     * 生成4位数的userId
     * @param userId 用户id
     * @return userId
     */
    private static String getUserId(String userId) {
        int len = userId.length();
        if (userId.length() < BIT_FOUR) {
            return String.format(FORMAT_USER_ID, Integer.valueOf(userId));
        } else {
            return userId.substring(len - BIT_FOUR, len);
        }
    }

}
