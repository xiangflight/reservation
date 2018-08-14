package com.lulu.reservation.domain.request;

import lombok.Data;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2018/8/14 下午3:34
 * <p>
 * 类说明：
 *     用户基础资料
 */
@Data
public class BaseInfoRequest {

    private String openid;

    private String nickname;

    private Integer sex;

    private Integer age;

    private Float tall;

    private Float weight;

    private Integer education;

    private Integer earn;

    private String hometown;

    public static BaseInfoRequest newInstance() {
        return new BaseInfoRequest();
    }

    @Override
    public String toString() {
        return "BaseInfoRequest{" +
                "openid='" + openid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                ", tall=" + tall +
                ", weight=" + weight +
                ", education=" + education +
                ", earn=" + earn +
                ", hometown='" + hometown + '\'' +
                '}';
    }
}
