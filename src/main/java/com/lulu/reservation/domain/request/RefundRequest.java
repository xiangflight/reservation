package com.lulu.reservation.domain.request;

import lombok.Getter;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2018/8/22 下午4:29
 * <p>
 * 类说明：
 *     退款请求
 */
@Getter
public class RefundRequest {

    private String openid;

    public static RefundRequest newInstance() {
        return new RefundRequest();
    }

    @Override
    public String toString() {
        return "RefundRequest{" +
                "openid='" + openid + '\'' +
                '}';
    }
}
