package com.lulu.reservation.service.sms.impl;

import com.alibaba.fastjson.JSON;
import com.lulu.reservation.comm.Constants;
import com.lulu.reservation.comm.config.JpushParameter;
import com.lulu.reservation.comm.exception.ParamException;
import com.lulu.reservation.domain.database.Sms;
import com.lulu.reservation.domain.response.Resp;
import com.lulu.reservation.repository.SmsRepository;
import com.lulu.reservation.service.sms.SmsService;
import com.lulu.reservation.util.PhoneFormatCheckUtil;
import com.lulu.reservation.util.RespUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/7/7 下午6:21
 * <p>
 * 类说明：
 *     短信服务具体实现
 */
@Slf4j
@Service
public class SmsServiceImpl implements SmsService {

    private final SmsRepository smsRepository;

    private final JpushParameter jpushParameter;

    @Autowired
    public SmsServiceImpl(SmsRepository smsRepository, JpushParameter jpushParameter) {
        this.smsRepository = smsRepository;
        this.jpushParameter = jpushParameter;
    }

    @Override
    public Resp sendSmsCode(String phone) {
        if (!PhoneFormatCheckUtil.isPhoneLegal(phone)){
            throw new ParamException(Constants.ApiErr.FORMAT_PHONE_ERROR);
        }
        sendRealCodeSms(phone);
        return RespUtil.successResp();
    }

    /**
     * 真实地发送短信验证码
     * @param phone 手机号码
     */
    private void sendRealCodeSms(String phone) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        String authorizedStr = Base64Utils.encodeToString(jpushParameter.getAuth().getBytes());
        httpHeaders.add(Constants.HTTP_AUTHORIZATION, Constants.HTTP_BASIC + authorizedStr);
        final MediaType mediaType = MediaType.parseMediaType(Constants.HTTP_MEDIA_TYPE);
        httpHeaders.setContentType(mediaType);
        httpHeaders.add(Constants.HTTP_ACCEPT, MediaType.APPLICATION_JSON.toString());
        HashMap<String, String> params = new HashMap<>(2);
        params.put(Constants.SMS_TEMPLATE_MOBILE, phone);
        params.put(Constants.SMS_TEMPLATE_ID, jpushParameter.getTempId());
        final String jsonParams = JSON.toJSONString(params);
        HttpEntity<String> formEntity = new HttpEntity<>(jsonParams, httpHeaders);
        String result = restTemplate.postForObject(jpushParameter.getUrl(), formEntity, String.class);
        log.info("send sms result = {}", result);
        saveSms(phone);
    }

    /**
     * 存储短信
     * @param phone 手机号
     */
    private void saveSms(String phone) {
        Sms sms = Sms.newInstance();
        sms.setPhone(phone);
        sms.setType(Constants.SmsType.VERIFICATION_CODE.ordinal());
        sms.setUpdateTime(System.currentTimeMillis());
        sms.setState(Constants.StatusErr.VALID.getCode());
        smsRepository.save(sms);
    }
}
