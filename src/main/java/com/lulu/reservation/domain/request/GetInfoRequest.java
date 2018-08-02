package com.lulu.reservation.domain.request;

import lombok.Data;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/8/2 下午6:20
 * <p>
 * 类说明：
 *     获取用户信息请求
 */
@Data
public class GetInfoRequest {

    private String openid;

    public static GetInfoRequest newInstance() {
        return new GetInfoRequest();
    }

    @Override
    public String toString() {
        return "GetInfoRequest{" +
                "openid=" + openid +
                '}';
    }
}
