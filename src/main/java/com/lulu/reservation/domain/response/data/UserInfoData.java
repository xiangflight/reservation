package com.lulu.reservation.domain.response.data;

import lombok.Data;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/8/2 下午6:12
 * <p>
 * 类说明：
 *     用户信息
 */
@Data
public class UserInfoData {

    private String nickname;

    private String image;

    private Boolean addinfo;

    public static UserInfoData newInstance() {
        return new UserInfoData();
    }

    @Override
    public String toString() {
        return "UserInfoData{" +
                "nickname='" + nickname + '\'' +
                ", image='" + image + '\'' +
                ", addinfo=" + addinfo +
                '}';
    }
}
