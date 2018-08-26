package com.lulu.reservation.domain.response.data;

import lombok.Data;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2018/8/18 下午12:14
 * <p>
 * 类说明：
 *     微信支付返回数据
 */
@Data
public class PayRepData {

    private String appId;

    private String timeStamp;

    private String nonceStr;

    private String packageValue;

    private String signType;

    private String paySign;

    public static PayRepData newInstance() {
        return new PayRepData();
    }

    @Override
    public String toString() {
        return "PayRepData{" +
                "appId='" + appId + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", nonceStr='" + nonceStr + '\'' +
                ", packageValue='" + packageValue + '\'' +
                ", signType='" + signType + '\'' +
                ", paySign='" + paySign + '\'' +
                '}';
    }
}
