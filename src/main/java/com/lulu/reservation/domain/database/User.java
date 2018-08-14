package com.lulu.reservation.domain.database;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

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

    private Boolean addInfo;

    private String avatar;

    private String nickname;

    private Integer sex;

    private Integer age;

    private Float tall;

    private Float weight;

    private Integer education;

    private Integer earn;

    private String hometown;

    private String description;

    private Integer occupation;

    private Integer constellation;

    private Integer drink;

    private Integer smoke;

    private Integer fitness;

    private String hobby;

    private String wechatId;

    private Integer state;

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
                ", addInfo=" + addInfo +
                ", avatar='" + avatar + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                ", tall=" + tall +
                ", weight=" + weight +
                ", education=" + education +
                ", earn=" + earn +
                ", hometown='" + hometown + '\'' +
                ", description='" + description + '\'' +
                ", occupation=" + occupation +
                ", constellation=" + constellation +
                ", drink=" + drink +
                ", smoke=" + smoke +
                ", fitness=" + fitness +
                ", hobby='" + hobby + '\'' +
                ", wechatId='" + wechatId + '\'' +
                ", state=" + state +
                '}';
    }
}
