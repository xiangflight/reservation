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
@Table(name = "t_sms")
public class Sms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String phone;

    @Column
    private Integer type;

    @Column(name = "update_time")
    private Long updateTime;

    @Column
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
}
