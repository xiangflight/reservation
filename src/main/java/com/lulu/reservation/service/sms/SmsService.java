package com.lulu.reservation.service.sms;

import com.lulu.reservation.domain.response.Resp;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/7/7 下午4:54
 * <p>
 * 类说明：
 *     短信服务层
 */

public interface SmsService {
    /**
     * 发送短信验证码
     * @param phone 手机号
     * @return 返回
     */
    Resp sendSmsCode(String phone);
}
