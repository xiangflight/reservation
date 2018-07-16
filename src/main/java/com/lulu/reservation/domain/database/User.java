package com.lulu.reservation.domain.database;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/7/15 下午6:02
 * <p>
 * 类说明：
 *     用户表
 */
@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 11)
    private String phone;

    @Column(nullable = false, length = 64)
    private String mpOpenid;

    private String mpNickname;

    private Integer mpSex;

    private String mpLanguage;

    private String mpCity;

    private String mpProvince;

    private String mpCountry;

    private String mpHeaderImg;

    private Long mpSubscribeTime;

    private String mpUnionId;

    private String mpRemark;

    private Integer mpGroupId;

    private String mpSubscribeScene;

    private Double latitude;

    private Double longitude;

    public static User newInstance() {
        return new User();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", mpOpenid='" + mpOpenid + '\'' +
                ", mpNickname='" + mpNickname + '\'' +
                ", mpSex=" + mpSex +
                ", mpLanguage='" + mpLanguage + '\'' +
                ", mpCity='" + mpCity + '\'' +
                ", mpProvince='" + mpProvince + '\'' +
                ", mpCountry='" + mpCountry + '\'' +
                ", mpHeaderImg='" + mpHeaderImg + '\'' +
                ", mpSubscribeTime=" + mpSubscribeTime +
                ", mpUnionId='" + mpUnionId + '\'' +
                ", mpRemark='" + mpRemark + '\'' +
                ", mpGroupId=" + mpGroupId +
                ", mpSubscribeScene='" + mpSubscribeScene + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
