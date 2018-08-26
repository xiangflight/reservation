package com.lulu.reservation.domain.request;

import lombok.Data;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2018/8/17 下午10:30
 * <p>
 * 类说明：
 *     支付请求类
 */
@Data
public class PayRequest {

    private String openid;

    @Override
    public String toString() {
        return "PayRequest{" +
                "openid='" + openid +
                '}';
    }
}
