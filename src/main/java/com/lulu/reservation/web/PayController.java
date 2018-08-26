package com.lulu.reservation.web;

import com.lulu.reservation.comm.Constants;
import com.lulu.reservation.comm.config.WechatParameter;
import com.lulu.reservation.domain.database.OrderRefund;
import com.lulu.reservation.domain.database.OrderReservation;
import com.lulu.reservation.domain.database.User;
import com.lulu.reservation.domain.request.PayRequest;
import com.lulu.reservation.domain.request.RefundRequest;
import com.lulu.reservation.domain.request.WxPayRequest;
import com.lulu.reservation.domain.response.Resp;
import com.lulu.reservation.domain.response.data.PayRepData;
import com.lulu.reservation.repository.OrderRefundRepository;
import com.lulu.reservation.repository.OrderRepository;
import com.lulu.reservation.repository.UserRepository;
import com.lulu.reservation.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2018/8/17 下午10:24
 * <p>
 * 类说明：
 *     微信公众号支付
 */
@RestController
@RequestMapping("/pay")
@Slf4j
public class PayController {

    private final UserRepository userRepository;

    private final WechatParameter wechatParameter;

    private final OrderRepository orderRepository;

    private final OrderRefundRepository orderRefundRepository;

    @Autowired
    public PayController(UserRepository userRepository, WechatParameter wechatParameter, OrderRepository orderRepository, OrderRefundRepository orderRefundRepository) {
        this.userRepository = userRepository;
        this.wechatParameter = wechatParameter;
        this.orderRepository = orderRepository;
        this.orderRefundRepository = orderRefundRepository;
    }

    @PostMapping("/refund")
    @Transactional(rollbackFor = Exception.class)
    public Resp refundFee(@RequestBody RefundRequest refundRequest) {
        final String openid = refundRequest.getOpenid();
        final User user = userRepository.findByMpOpenid(openid);
        user.setMoney(new BigDecimal(0.00));
        userRepository.save(user);
        OrderRefund orderRefund = new OrderRefund();
        orderRefund.setUserId(user.getId());
        orderRefund.setWechatId(user.getWechatId());
        orderRefund.setPhone(user.getPhone());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
        String refundTime = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        orderRefund.setRefundTime(refundTime);
        orderRefundRepository.save(orderRefund);
        return RespUtil.successResp();
    }

    @PostMapping("/guarantee")
    @Transactional(rollbackFor = Exception.class)
    public Resp payGuarantee(@RequestBody PayRequest payRequest, HttpServletRequest httpServletRequest) {
        final String openid = payRequest.getOpenid();
        String spBillCreateIp = ClientIpGetUtil.getIpAddress(httpServletRequest);
        final User user = userRepository.findByMpOpenid(openid);
        String outTradeNo = OrderIdGenerator.getOrderNo(0, user.getId() + "");
        PayRepData payRepData = PayRepData.newInstance();
        String body = "我很中意nei-守约金";
        String notifyUrl = "http://zynei.com/reservation/pay/notify";
        String guaranteeFee = "10";
        String response = constructMpRequest(guaranteeFee, body, outTradeNo, spBillCreateIp, openid, notifyUrl);
        WxPayRequest wxPayResponse = (WxPayRequest) XmlUtil.fromXML(response);
        String timeStamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);
        payRepData.setAppId(wechatParameter.getId());
        payRepData.setTimeStamp(timeStamp);
        payRepData.setNonceStr(wxPayResponse.getNonceStr());
        payRepData.setPackageValue("prepay_id=" + wxPayResponse.getPrepayId());
        payRepData.setSignType("MD5");
        String paySign = "appId=" + wxPayResponse.getAppid() +
                "&nonceStr=" + wxPayResponse.getNonceStr() +
                "&package=prepay_id=" + wxPayResponse.getPrepayId() +
                "&signType=MD5" +
                "&timeStamp=" + timeStamp;
        paySign = SignatureUtil.getSign(paySign, wechatParameter.getKey());
        payRepData.setPaySign(paySign);
        return RespUtil.successResp(payRepData);
    }

    private String constructMpRequest(String fee, String body, String outTradeNo, String spBillCreateIp, String openid, String notifyUrl) {
        try {
            HashMap<String, String> paramMap = new HashMap<>();
            String appId = wechatParameter.getId();
            String mchId = wechatParameter.getMchid();
            String randomStr = AlphabetRandom.random(32);
            paramMap.put("appid", appId);
            paramMap.put("mch_id", mchId);
            paramMap.put("nonce_str", randomStr);
            paramMap.put("body", body);
            paramMap.put("out_trade_no", outTradeNo);
            paramMap.put("total_fee", fee);
            paramMap.put("spbill_create_ip", spBillCreateIp);
            paramMap.put("notify_url", notifyUrl);
            paramMap.put("trade_type", "JSAPI");
            paramMap.put("openid", openid);
            String str = MapUtil.join(paramMap);
            String sign = SignatureUtil.getSign(str, wechatParameter.getKey());
            WxPayRequest wxPayRequest = WxPayRequest.newInstance();
            wxPayRequest.setAppid(appId);
            wxPayRequest.setMchId(mchId);
            wxPayRequest.setNonceStr(randomStr);
            wxPayRequest.setBody(body);
            wxPayRequest.setOutTradeNo(outTradeNo);
            wxPayRequest.setTotalFee(Integer.valueOf(fee));
            wxPayRequest.setSpbillCreateIp(spBillCreateIp);
            wxPayRequest.setNotifyUrl(notifyUrl);
            wxPayRequest.setTradeType("JSAPI");
            wxPayRequest.setOpenid(openid);
            wxPayRequest.setSign(sign);
            String xmlStr = XmlUtil.toXMl(wxPayRequest);
            xmlStr = new String(xmlStr.getBytes("UTF-8"), "ISO-8859-1");
            log.info("wxpay request:\n{}", xmlStr);
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.postForObject(Constants.WX_MP_PAY_URL, xmlStr, String.class);
            response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
            log.info("wxpay response:\n{}", response);
            return response;
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException" + e);
            return null;
        } catch (Exception e) {
            log.error("WxpayService wxpay unifiedorder failed." + e);
            return null;
        }
    }

    @RequestMapping("/notify")
    @Transactional(rollbackFor = Exception.class)
    public String payNotify(HttpServletRequest request) {
        String failXml = "<xml>\n" +
                "  <return_code><![CDATA[FAIL]]></return_code>\n" +
                "</xml>";
        try {
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String response = stringBuilder.toString();
            log.info("微信支付回调接口- 相应参数 request: \n{}", response);
            WxPayRequest wxPayResponse = (WxPayRequest) XmlUtil.fromXML(response);
            final String openid = wxPayResponse.getOpenid();
            User user = userRepository.findByMpOpenid(openid);
            final Long userId = user.getId();
            final String outTradeNo = wxPayResponse.getOutTradeNo();
            // create a new order.
            OrderReservation order = OrderReservation.newInstance();
            order.setSerialNo(outTradeNo);
            order.setUserId(userId);
            order.setState(1);
            orderRepository.save(order);
            // 用户金额上要加上内容
            BigDecimal userMoney = user.getMoney();
            if (userMoney == null ) {
                userMoney = new BigDecimal(0.00);
            }
            Integer totalFee = wxPayResponse.getTotalFee();
            totalFee = 9900;
            user.setMoney(userMoney.add(new BigDecimal(totalFee / 100)));
            userRepository.save(user);
        } catch (Exception e) {
            log.error("error message: {}", e.getMessage());
            return null;
        }
        return "<xml>\n" +
                "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                "</xml>";
    }

}
