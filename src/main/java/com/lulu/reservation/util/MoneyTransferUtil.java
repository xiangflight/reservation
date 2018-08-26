package com.lulu.reservation.util;

import java.math.BigDecimal;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2018/8/18 下午1:01
 * <p>
 * 类说明：
 *     金额转换工具类
 */

public class MoneyTransferUtil {

    public static int cnyToFen(BigDecimal CNY) {
        return new BigDecimal(100).multiply(CNY).intValue();
    }
}
