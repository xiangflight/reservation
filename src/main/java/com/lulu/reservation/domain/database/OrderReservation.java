package com.lulu.reservation.domain.database;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2018/8/18 下午12:05
 * <p>
 * 类说明：
 *     订单
 */
@Data
@Entity
public class OrderReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String serialNo;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Integer state;

    public static OrderReservation newInstance() {
        return new OrderReservation();
    }

    @Override
    public String toString() {
        return "OrderReservation{" +
                "id=" + id +
                ", serialNo='" + serialNo + '\'' +
                ", userId=" + userId +
                ", state=" + state +
                '}';
    }
}
