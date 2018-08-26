package com.lulu.reservation.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2018/8/25 下午9:27
 * <p>
 * 类说明：
 *     时间工具类
 */

public class TimeUtil {

    /**
     * 将日期格式字符串转化为时间戳
     * @param dateStr 时间格式字符串
     * @param format 日期格式
     * @return 时间戳
     */
    public static Long date2TimeStamp(String dateStr, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            return simpleDateFormat.parse(dateStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("转化时间格式字符串失败");
    }

}
