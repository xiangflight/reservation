package com.lulu.reservation.domain.request;

import lombok.Data;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2018/8/22 下午3:02
 * <p>
 * 类说明：
 *     约会详情
 */
@Data
public class ReservationRequest {

    private Long id;

    public static ReservationRequest newInstance() {
        return new ReservationRequest();
    }

    @Override
    public String toString() {
        return "ReservationRequest{" +
                "id=" + id +
                '}';
    }
}
