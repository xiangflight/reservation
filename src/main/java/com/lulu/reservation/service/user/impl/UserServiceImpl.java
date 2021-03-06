package com.lulu.reservation.service.user.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lulu.reservation.comm.Constants;
import com.lulu.reservation.comm.config.JpushParameter;
import com.lulu.reservation.comm.exception.ParamException;
import com.lulu.reservation.domain.database.Reservation;
import com.lulu.reservation.domain.database.Sms;
import com.lulu.reservation.domain.database.User;
import com.lulu.reservation.domain.request.BaseInfoRequest;
import com.lulu.reservation.domain.request.LoginRequest;
import com.lulu.reservation.domain.request.MoreInfoRequest;
import com.lulu.reservation.domain.request.ReservationRequest;
import com.lulu.reservation.domain.response.Resp;
import com.lulu.reservation.domain.response.data.ReservationData;
import com.lulu.reservation.domain.response.data.UserInfoData;
import com.lulu.reservation.repository.ReservationRepository;
import com.lulu.reservation.repository.SmsRepository;
import com.lulu.reservation.repository.UserRepository;
import com.lulu.reservation.service.user.IUserService;
import com.lulu.reservation.util.RespUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Optional;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/7/16 下午8:04
 * <p>
 * 类说明：
 *     用户服务实现类
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    private final SmsRepository smsRepository;

    private final JpushParameter jpushParameter;

    private final UserRepository userRepository;

    private final ReservationRepository reservationRepository;

    @Autowired
    public UserServiceImpl(SmsRepository smsRepository, JpushParameter jpushParameter, UserRepository userRepository, ReservationRepository reservationRepository) {
        this.smsRepository = smsRepository;
        this.jpushParameter = jpushParameter;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Resp login(LoginRequest loginRequest) {
        // 1. 首先验证验证码有没有效
        final String code = loginRequest.getCode();
        final String phone = loginRequest.getPhone();
        final Sms sms = smsRepository.findFirstByPhoneOrderByIdDesc(phone);
        log.info("sms: {}", sms);
        String msgId = sms.getMsgId();
        log.info("code: {}, msgId: {}", code, msgId);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        String authorizedStr = Base64Utils.encodeToString(jpushParameter.getAuth().getBytes());
        httpHeaders.add(Constants.HTTP_AUTHORIZATION, Constants.HTTP_BASIC + authorizedStr);
        final MediaType mediaType = MediaType.parseMediaType(Constants.HTTP_MEDIA_TYPE);
        httpHeaders.setContentType(mediaType);
        httpHeaders.add(Constants.HTTP_ACCEPT, MediaType.APPLICATION_JSON.toString());
        HashMap<String, String> params = new HashMap<>(1);
        params.put("code", code);
        final String jsonParams = JSON.toJSONString(params);
        HttpEntity<String> formEntity = new HttpEntity<>(jsonParams, httpHeaders);
        String postUrl = String.format(jpushParameter.getValidate(), msgId);
        log.info("post url: {}", postUrl);
        String result = restTemplate.postForObject(postUrl, formEntity, String.class);
        log.info("validate result: {}", result);
        JSONObject resultJson = JSON.parseObject(result);
        Boolean isValid = resultJson.getBoolean("is_valid");
        log.info("isValid: {}", isValid);
        if (!isValid) {
            throw new ParamException(Constants.ApiErr.VERIFICATION_CODE_ERROR);
        }
        // 2. 验证通过，将经纬度，号码存入表中
        final String openid = loginRequest.getOpenid();
        log.info("openid: {}", openid);
        final User user = userRepository.findByMpOpenid(openid);
        log.info("user: {}", user);
        user.setPhone(phone);
        final Double latitude = loginRequest.getLatitude();
        final Double longitude = loginRequest.getLongitude();
        user.setLongitude(longitude);
        user.setLatitude(latitude);
        log.info("latitude: {}, longtitude: {}", latitude, longitude);
        final User saveUser = userRepository.save(user);
        return RespUtil.successResp(saveUser.getId());
    }

    @Override
    public Resp info(String openId) {
        final User user = userRepository.findByMpOpenid(openId);
        UserInfoData userInfoData = UserInfoData.newInstance();
        userInfoData.setNickname(user.getMpNickname());
        userInfoData.setImage(user.getMpHeaderImg());
        userInfoData.setAddinfo(user.getAddInfo());
        return RespUtil.successResp(userInfoData);
    }

    @Override
    public Resp baseInfo(BaseInfoRequest baseInfoRequest) {
        final User user = userRepository.findByMpOpenid(baseInfoRequest.getOpenid());
        user.setNickname(baseInfoRequest.getNickname());
        user.setSex(baseInfoRequest.getSex());
        user.setAge(baseInfoRequest.getAge());
        user.setTall(baseInfoRequest.getTall());
        user.setWeight(baseInfoRequest.getWeight());
        user.setEducation(baseInfoRequest.getEducation());
        user.setEarn(baseInfoRequest.getEarn());
        user.setHometown(baseInfoRequest.getHometown());
        userRepository.save(user);
        return RespUtil.successResp();
    }

    @Override
    public Resp moreInfo(MoreInfoRequest moreInfoRequest) {
        final User user = userRepository.findByMpOpenid(moreInfoRequest.getOpenid());
        user.setDescription(moreInfoRequest.getDescription());
        user.setOccupation(moreInfoRequest.getOccupation());
        user.setConstellation(moreInfoRequest.getConstellation());
        user.setDrink(moreInfoRequest.getDrink());
        user.setSmoke(moreInfoRequest.getSmoke());
        user.setFitness(moreInfoRequest.getFitness());
        user.setHobby(moreInfoRequest.getHobby());
        user.setWechatId(moreInfoRequest.getWechatId());
        userRepository.save(user);
        return RespUtil.successResp();
    }

    @Override
    public Resp reservationInfo(ReservationRequest reservationRequest) {
        final Long reservationId = reservationRequest.getId();
        Reservation reservation = reservationRepository.findById(reservationId).get();
        ReservationData reservationData = new ReservationData();
        reservationData.setLocation(reservation.getLocation());
        reservationData.setTime(reservation.getReserveTime());
        final Long otherId = reservation.getOther();
        final User user = userRepository.findById(otherId).get();
        reservationData.setImage(user.getAvatar());
        reservationData.setOpenid(user.getMpOpenid());
        return RespUtil.successResp(reservationData);
    }
}
