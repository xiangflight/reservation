package com.lulu.reservation.domain.database;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/7/7 下午4:36
 * <p>
 * 类说明：
 *     短信实体类
 */
@Data
@Entity
public class Sms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private Integer type;

    @Column(nullable = false)
    private Long updateTime;

    @Column(nullable = false)
    private Integer state;

    @Override
    public String toString() {
        return "Sms{" +
                "phone='" + phone + '\'' +
                ", type=" + type +
                ", updateTime=" + updateTime +
                ", state=" + state +
                '}';
    }

    public static Sms newInstance() {
        return new Sms();
    }
}
