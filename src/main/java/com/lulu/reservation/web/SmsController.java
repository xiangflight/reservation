package com.lulu.reservation.web;

import com.lulu.reservation.comm.Constants;
import com.lulu.reservation.comm.exception.ParamException;
import com.lulu.reservation.domain.request.SmsRequest;
import com.lulu.reservation.domain.response.Resp;
import com.lulu.reservation.service.sms.ISmsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/7/7 下午4:54
 * <p>
 * 类说明：
 *     短信Controller
 */
@Slf4j
@RestController
@RequestMapping("/sms")
public class SmsController {

    private final ISmsService smsService;

    @Autowired
    public SmsController(ISmsService smsService) {
        this.smsService= smsService;
    }

    @PostMapping("/verification/code")
    public Resp sendVerificationCode(@RequestBody SmsRequest smsRequest) {
        final String phone = smsRequest.getPhone();
        if (StringUtils.isEmpty(phone)) {
            throw new ParamException(Constants.ApiErr.EMPTY_PHONE_ERROR);
        }
        return smsService.sendSmsCode(phone);
    }

}
