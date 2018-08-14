package com.lulu.reservation.domain.request;

import lombok.Data;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2018/8/14 下午4:21
 * <p>
 * 类说明：
 *     用户附加信息
 */
@Data
public class MoreInfoRequest {

    private String openid;

    private String description;

    private Integer occupation;

    private Integer constellation;

    private Integer drink;

    private Integer smoke;

    private Integer fitness;

    private String hobby;

    private String wechatId;

    public static MoreInfoRequest newInstance() {
        return new MoreInfoRequest();
    }

    @Override
    public String toString() {
        return "MoreInfoRequest{" +
                "openid='" + openid + '\'' +
                ", description='" + description + '\'' +
                ", occupation=" + occupation +
                ", constellation=" + constellation +
                ", drink=" + drink +
                ", smoke=" + smoke +
                ", fitness=" + fitness +
                ", hobby='" + hobby + '\'' +
                ", wechatId='" + wechatId + '\'' +
                '}';
    }
}
