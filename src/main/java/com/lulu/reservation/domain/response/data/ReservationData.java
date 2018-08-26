package com.lulu.reservation.domain.response.data;

import lombok.Data;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/8/2 下午6:12
 * <p>
 * 类说明：
 *     约会Response
 */
@Data
public class ReservationData {

    private String openid;

    private String image;

    private String time;

    private String location;

    public static ReservationData newInstance() {
        return new ReservationData();
    }

    @Override
    public String toString() {
        return "ReservationData{" +
                "openid=" + openid +
                ", image='" + image + '\'' +
                ", time='" + time + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
