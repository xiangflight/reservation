package com.lulu.reservation.domain.database;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2018/8/22 下午1:19
 * <p>
 * 类说明：
 *     约会记录
 */
@Data
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long one;

    @Column(nullable = false)
    private Long other;

    @Column(nullable = false)
    private String reserveTime;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Integer state;

    public static Reservation newInstance() {
        return new Reservation();
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", one=" + one +
                ", other=" + other +
                ", reserveTime='" + reserveTime + '\'' +
                ", location='" + location + '\'' +
                ", state=" + state +
                '}';
    }
}
