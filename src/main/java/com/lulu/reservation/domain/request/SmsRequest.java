package com.lulu.reservation.domain.request;

import lombok.Data;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/7/7 下午5:57
 * <p>
 * 类说明：
 *     请求类
 */
@Data
public class SmsRequest {

    private String phone;

    @Override
    public String toString() {
        return "SmsRequest{" +
                "phone='" + phone + '\'' +
                '}';
    }
}
