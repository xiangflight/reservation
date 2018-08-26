package com.lulu.reservation.domain.database;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2018/8/22 下午4:30
 * <p>
 * 类说明：
 *    退款记录
 */
@Data
@Entity
public class OrderRefund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String wechatId;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String refundTime;

    public static OrderRefund newInstance() {
        return new OrderRefund();
    }

    @Override
    public String toString() {
        return "OrderRefund{" +
                "id=" + id +
                ", userId=" + userId +
                ", wechatId='" + wechatId + '\'' +
                ", phone='" + phone + '\'' +
                ", refundTime='" + refundTime + '\'' +
                '}';
    }
}
