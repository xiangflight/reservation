package com.lulu.reservation.domain.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2018/8/18 下午1:17
 * <p>
 * 类说明：
 *     微信支付请求
 */
@XStreamAlias(value = "xml")
@Data
public class WxPayRequest {

    private String appid;

    private String body;

    private String sign;

    private String openid;

    @XStreamAlias(value = "mch_id")
    private String mchId;

    @XStreamAlias(value = "nonce_str")
    private String nonceStr;

    @XStreamAlias(value = "notify_url")
    private String notifyUrl;

    @XStreamAlias(value = "out_trade_no")
    private String outTradeNo;

    @XStreamAlias(value = "spbill_create_ip")
    private String spbillCreateIp;

    @XStreamAlias(value = "total_fee")
    private Integer totalFee;

    @XStreamAlias(value = "trade_type")
    private String tradeType;

    @XStreamAlias(value = "return_code")
    private String returnCode;

    @XStreamAlias(value = "prepay_id")
    private String prepayId;

    public static WxPayRequest newInstance() {
        return new WxPayRequest();
    }

    @Override
    public String toString() {
        return "WxPayRequest{" +
                "appid='" + appid + '\'' +
                ", body='" + body + '\'' +
                ", sign='" + sign + '\'' +
                ", openid='" + openid + '\'' +
                ", mchId='" + mchId + '\'' +
                ", nonceStr='" + nonceStr + '\'' +
                ", notifyUrl='" + notifyUrl + '\'' +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", spbillCreateIp='" + spbillCreateIp + '\'' +
                ", totalFee=" + totalFee +
                ", tradeType='" + tradeType + '\'' +
                ", returnCode='" + returnCode + '\'' +
                ", prepayId='" + prepayId + '\'' +
                '}';
    }
}
