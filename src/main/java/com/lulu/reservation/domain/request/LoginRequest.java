package com.lulu.reservation.domain.request;

import lombok.Data;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/7/16 下午8:06
 * <p>
 * 类说明：
 *     登录请求类
 */
@Data
public class LoginRequest {

    private String phone;

    private String code;

    private String openid;

    private Double longitude;

    private Double latitude;

    public static LoginRequest newInstance() {
        return new LoginRequest();
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "phone='" + phone + '\'' +
                ", code='" + code + '\'' +
                ", openid='" + openid + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}