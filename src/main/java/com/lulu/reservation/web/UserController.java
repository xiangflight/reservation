package com.lulu.reservation.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lulu.reservation.comm.Constants;
import com.lulu.reservation.comm.algo.AgeRange;
import com.lulu.reservation.comm.algo.HeightRange;
import com.lulu.reservation.comm.algo.MatchCondition;
import com.lulu.reservation.comm.algo.WeightRange;
import com.lulu.reservation.comm.config.WechatParameter;
import com.lulu.reservation.domain.database.Reservation;
import com.lulu.reservation.domain.database.User;
import com.lulu.reservation.domain.request.*;
import com.lulu.reservation.domain.response.Resp;
import com.lulu.reservation.repository.ReservationRepository;
import com.lulu.reservation.repository.UserRepository;
import com.lulu.reservation.service.user.IUserService;
import com.lulu.reservation.util.RespUtil;
import com.lulu.reservation.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/7/16 下午8:01
 * <p>
 * 类说明：
 * 用户Controller
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    private final IUserService userService;

    private final UserRepository userRepository;

    private final WechatParameter wechatParameter;

    private final ReservationRepository reservationRepository;

    @Autowired
    public UserController(IUserService userService, UserRepository userRepository, WechatParameter wechatParameter, ReservationRepository reservationRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.wechatParameter = wechatParameter;
        this.reservationRepository = reservationRepository;
    }

    @PostMapping("/login")
    public Resp login(@RequestBody LoginRequest loginRequest) {
        log.info("{}", loginRequest);
        return userService.login(loginRequest);
    }

    @PostMapping("/info")
    public Resp info(@RequestBody GetInfoRequest getInfoRequest) {
        final String openId = getInfoRequest.getOpenid();
        log.info("Post Request is /info/{}", openId);
        return userService.info(openId);
    }

    @PostMapping("/img/upload/{openid}")
    public Resp fileUpload(@RequestParam("file") MultipartFile file, @PathVariable("openid") String openid) {
        if (file.isEmpty()) {
            return RespUtil.errorResp(Constants.StatusErr.INVALID);
        }
        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(Constants.UPLOAD_FILE_DIR + file.getOriginalFilename());
            Files.write(path, bytes);
            final User user = userRepository.findByMpOpenid(openid);
            user.setAvatar(Constants.AVATAR_PREFIX + file.getOriginalFilename());
            userRepository.save(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return RespUtil.successResp();
    }

    @PostMapping("/get/info/reservation")
    public Resp reservationInfo(@RequestBody ReservationRequest reservationRequest) {
        return userService.reservationInfo(reservationRequest);
    }

    @PostMapping("/put/info/base")
    public Resp baseInfo(@RequestBody BaseInfoRequest baseInfoRequest) {
        return userService.baseInfo(baseInfoRequest);
    }

    @PostMapping("/put/info/more")
    public Resp moreInfo(@RequestBody MoreInfoRequest moreInfoRequest) {
        return userService.moreInfo(moreInfoRequest);
    }

    @PostMapping("/info/get")
    public Resp getInfo(@RequestBody GetInfoRequest getInfoRequest) {
        final String openid = getInfoRequest.getOpenid();
        return RespUtil.successResp(userRepository.findByMpOpenid(openid));
    }

    @PostMapping("/match")
    @Transactional(rollbackFor = Exception.class)
    public Resp match(@RequestBody MatchRequest matchRequest) {
        final String openid = matchRequest.getOpenid();
        MatchCondition matchCondition = constructMatchCondition(matchRequest);
        // 加入匹配池中
        User curUser = userRepository.findByMpOpenid(openid);
        log.info("curUser is {}", curUser.getId());
        final Integer sex = curUser.getSex();
        log.info("current sex：{}", sex);
        int other = sex == 1 ? 2 : 1;
        long now = System.currentTimeMillis();
        log.info("now is {}", now);
        Integer save = matchRequest.getSave();
        if (save == 1) {
            curUser.setState(0);
        } else {
            curUser.setState(1);
        }
        curUser.setTime(matchRequest.getTime());
        curUser.setArea(matchRequest.getArea());
        curUser.setAgeif(matchRequest.getAge());
        curUser.setHeightif(matchRequest.getHeight());
        curUser.setWeightif(matchRequest.getWeight());
        curUser.setEducationif(matchRequest.getEducation());
        curUser.setEarnif(matchRequest.getEarn());
        User saveUser = userRepository.save(curUser);
        log.info("user saved: {}", saveUser);
        if (save == 1) {
            return RespUtil.successResp();
        }
        log.info("active current user");
        while (true) {
            // check state
            if (curUser.getState() == 2) {
                // exit loop
                final Long reservationId = curUser.getReservationId();
                if (reservationId != null) {
                    Reservation matchReservation = reservationRepository.findById(reservationId).get();
                    return RespUtil.successResp(matchReservation);
                }
            }
            if (System.currentTimeMillis() - now >= 10 * 3600 * 1000) {
                log.info("difference: {}", System.currentTimeMillis() - now);
                break;
            }
            List<User> users = userRepository.findByState(1);
            for (User user : users) {
                if (user.getSex() != other) {
                    continue;
                }
                final Integer age = user.getAge();
                final Float tall = user.getTall();
                final Float weight = user.getWeight();
                final Integer education = user.getEducation();
                final Integer earn = user.getEarn();
                final Integer time = user.getTime();
                final Integer area = user.getArea();
                log.info("user: age = {}, tall = {}, weight = {}, education = {}, earn = {}, time = {}, area = {}", age, tall, weight, education, earn, time, area);
                log.info("age range: {}, match result: {}", matchCondition.getAgeRange(), matchCondition.getAgeRange().isBetween(age));
                log.info("height range: {}, match result: {}", matchCondition.getHeightRange(), matchCondition.getHeightRange().isBetween(tall));
                log.info("weight range: {}, match result: {}", matchCondition.getWeightRange(), matchCondition.getWeightRange().isBetween(weight));
                log.info("education: {}, match result: {}", matchCondition.getEducation(), matchCondition.getEducation() == education);
                log.info("earn: {}, match result: {}", matchCondition.getEarn(), matchCondition.getEarn() == earn);
                log.info("time: {}, match result: {}", matchCondition.getTime(), matchCondition.getTime() == time);
                log.info("area: {}, match result: {}", matchCondition, matchCondition.getArea() == area);
                if (matchCondition.getAgeRange().isBetween(age) &&
                        matchCondition.getHeightRange().isBetween(tall) &&
                        matchCondition.getWeightRange().isBetween(weight) &&
                        matchCondition.getEducation() == education &&
                        matchCondition.getEarn() == earn &&
                        matchCondition.getTime() == time &&
                        matchCondition.getArea() == area
                ) {
                    log.info("match successfully");
                    Reservation reservation = new Reservation();
                    reservation.setOne(curUser.getId());
                    reservation.setOther(user.getId());
                    reservation.setLocation(area == 1 ?
                            "南山区科技园中科研发园三号楼裙楼302室米萝咖啡(TCL大厦正门对面)" :
                            "福田区车公庙F口Ncafe花园餐厅");
                    reservation.setReserveTime(getRealTime(time));
                    reservation.setState(1);
                    final Reservation saveReservation = reservationRepository.save(reservation);
                    sendTemplateMessage(saveReservation);
                    Long reservationId = saveReservation.getId();
                    user.setReservationId(reservationId);
                    user.setState(2);
                    curUser.setReservationId(reservationId);
                    curUser.setState(2);
                    userRepository.save(user);
                    userRepository.save(curUser);
                    return RespUtil.successResp(saveReservation);
                }
            }
        }
        log.info("match failure");
        curUser.setState(0);
        userRepository.save(curUser);
        sendTemplateMessage(curUser);
        return RespUtil.errorResp(Constants.StatusErr.INVALID);
    }

    private MatchCondition constructMatchCondition(MatchRequest matchRequest) {
        MatchCondition matchCondition = new MatchCondition();
        final Integer age = matchRequest.getAge();
        AgeRange ageRange = null;
        switch (age) {
            case 1:
                ageRange = new AgeRange(18, 20);
                break;
            case 2:
                ageRange = new AgeRange(20, 23);
                break;
            case 3:
                ageRange = new AgeRange(23, 25);
                break;
            case 4:
                ageRange = new AgeRange(25, 28);
                break;
            case 5:
                ageRange = new AgeRange(28, 30);
                break;
            case 6:
                ageRange = new AgeRange(30, 35);
                break;
            case 7:
                ageRange = new AgeRange(35, 40);
                break;
            default:
                break;
        }
        matchCondition.setAgeRange(ageRange);
        final Integer height = matchRequest.getHeight();
        HeightRange heightRange = null;
        switch (height) {
            case 1:
                heightRange = new HeightRange(140.0f, 150.0f);
                break;
            case 2:
                heightRange = new HeightRange(150.0f, 155.0f);
                break;
            case 3:
                heightRange = new HeightRange(155.0f, 160.0f);
                break;
            case 4:
                heightRange = new HeightRange(160.0f, 165.0f);
                break;
            case 5:
                heightRange = new HeightRange(165.0f, 170.0f);
                break;
            case 6:
                heightRange = new HeightRange(170.0f, 175.0f);
                break;
            case 7:
                heightRange = new HeightRange(175.0f, 180.0f);
                break;
            case 8:
                heightRange = new HeightRange(180.0f, 190.0f);
                break;
            case 9:
                heightRange = new HeightRange(190.0f, 200.0f);
                break;
            default:
                break;
        }
        matchCondition.setHeightRange(heightRange);
        final Integer weight = matchRequest.getWeight();
        WeightRange weightRange = null;
        switch (weight) {
            case 1:
                weightRange = new WeightRange(40.0f, 45.0f);
                break;
            case 2:
                weightRange = new WeightRange(45.0f, 50.0f);
                break;
            case 3:
                weightRange = new WeightRange(50.0f, 55.0f);
                break;
            case 4:
                weightRange = new WeightRange(55.0f, 60.0f);
                break;
            case 5:
                weightRange = new WeightRange(60.0f, 70.0f);
                break;
            case 6:
                weightRange = new WeightRange(70.0f, 80.0f);
                break;
            case 7:
                weightRange = new WeightRange(80.0f, 90.0f);
                break;
            default:
                break;
        }
        matchCondition.setWeightRange(weightRange);
        matchCondition.setEducation(matchRequest.getEducation());
        matchCondition.setEarn(matchRequest.getEarn());
        matchCondition.setTime(matchRequest.getTime());
        matchCondition.setArea(matchRequest.getArea());
        return matchCondition;
    }

    /**
     * 匹配失败模版消息给用户
     * @param user 用户
     */
    private void sendTemplateMessage(User user) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        final MediaType mediaType = MediaType.parseMediaType(Constants.HTTP_MEDIA_TYPE);
        httpHeaders.setContentType(mediaType);
        String getTokenUrl = String.format(Constants.WX_MP_GET_ACCESS_TOKEN_URL, wechatParameter.getId(), wechatParameter.getSecret());
        String response = restTemplate.getForObject(getTokenUrl, String.class);
        log.info("token response: {}", response);
        JSONObject accessTokenJson = JSON.parseObject(response);
        String accessToken = accessTokenJson.getString("access_token");
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken;
        String requestBody = "{\n" +
                "           \"touser\":\"" + user.getMpOpenid() + "\",\n" +
                "           \"template_id\":\"" + Constants.MESSAGE_TEMPLATE_FAILURE + "\",\n" +
                "           \"data\":{\n" +
                "                   \"first\": {\n" +
                "                       \"value\":\"" + Constants.FIRST_MESSAGE_LINE_FAILURE + "\",\n" +
                "                       \"color\":\"#173177\"\n" +
                "                   },\n" +
                "                   \"keyword1\":{\n" +
                "                       \"value\":\"" + "基础匹配" + "\",\n" +
                "                       \"color\":\"#173177\"\n" +
                "                   },\n" +
                "                   \"keyword2\": {\n" +
                "                       \"value\":\"" + "未找到符合条件的匹配对象" + "\",\n" +
                "                       \"color\":\"#173177\"\n" +
                "                   },\n" +
                "                   \"keyword3\": {\n" +
                "                       \"value\":\"" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日HH:mm")) + "\",\n" +
                "                       \"color\":\"#173177\"\n" +
                "                   },\n" +
                "                   \"remark\":{\n" +
                "                       \"value\":\"" + Constants.REMARK_MESSAGE_LINE_FAILURE + "\",\n" +
                "                       \"color\":\"#173177\"\n" +
                "                   }\n" +
                "           }\n" +
                "       }";
        HttpEntity<String> entity = new HttpEntity<>(requestBody, httpHeaders);
        response = restTemplate.postForObject(url, entity, String.class);
        log.info("send template response: {}", response);
    }


    /**
     * 推送模板消息
     *
     * @param reservation 向参与约会的用户发送模版消息
     */
    private void sendTemplateMessage(Reservation reservation) {
        // get token
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        final MediaType mediaType = MediaType.parseMediaType(Constants.HTTP_MEDIA_TYPE);
        httpHeaders.setContentType(mediaType);
        String getTokenUrl = String.format(Constants.WX_MP_GET_ACCESS_TOKEN_URL, wechatParameter.getId(), wechatParameter.getSecret());
        String response = restTemplate.getForObject(getTokenUrl, String.class);
        log.info("token response: {}", response);
        JSONObject accessTokenJson = JSON.parseObject(response);
        String accessToken = accessTokenJson.getString("access_token");
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken;
        final Long oneUserId = reservation.getOne();
        final Long otherUserId = reservation.getOther();
        final String oneUserOpenId = userRepository.findById(oneUserId).get().getMpOpenid();
        final String oneUserNickName = userRepository.findById(oneUserId).get().getNickname();
        final String otherUserOpenId = userRepository.findById(otherUserId).get().getMpOpenid();
        final String otherUserNickName = userRepository.findById(otherUserId).get().getNickname();
        String location = reservation.getLocation();
        String time = reservation.getReserveTime();
        Long reservationId = reservation.getId();
        sendMessageTemplate(location, time, oneUserNickName, oneUserOpenId, reservationId, httpHeaders, restTemplate, url);
        sendMessageTemplate(location, time, otherUserNickName, otherUserOpenId, reservationId, httpHeaders, restTemplate, url);
    }

    private void sendMessageTemplate(String location, String time, String nickName, String openid, Long reservationId, HttpHeaders httpHeaders, RestTemplate restTemplate, String url) {
        String requestBody = "{\n" +
                "           \"touser\":\"" + openid + "\",\n" +
                "           \"template_id\":\"" + Constants.MESSAGE_TEMPLATE_ID + "\",\n" +
                "           \"url\":\"" + "http://zynei.com/reservation/appointment?id=" + reservationId + "\",  \n" +
                "           \"data\":{\n" +
                "                   \"first\": {\n" +
                "                       \"value\":\"" + Constants.FIRST_MESSAGE_LINE + "\",\n" +
                "                       \"color\":\"#173177\"\n" +
                "                   },\n" +
                "                   \"keyword1\":{\n" +
                "                       \"value\":\"" + "基础匹配" + "\",\n" +
                "                       \"color\":\"#173177\"\n" +
                "                   },\n" +
                "                   \"keyword2\": {\n" +
                "                       \"value\":\"" + time + "\",\n" +
                "                       \"color\":\"#173177\"\n" +
                "                   },\n" +
                "                   \"keyword3\": {\n" +
                "                       \"value\":\"" + location + "\",\n" +
                "                       \"color\":\"#173177\"\n" +
                "                   },\n" +
                "                   \"keyword4\": {\n" +
                "                       \"value\":\"" + nickName + "\",\n" +
                "                       \"color\":\"#173177\"\n" +
                "                   },\n" +
                "                   \"remark\":{\n" +
                "                       \"value\":\"" + Constants.REMARK_MESSAGE_LINE + "\",\n" +
                "                       \"color\":\"#173177\"\n" +
                "                   }\n" +
                "           }\n" +
                "       }";
        HttpEntity<String> entity = new HttpEntity<>(requestBody, httpHeaders);
        String response = restTemplate.postForObject(url, entity, String.class);
        log.info("send template response: {}", response);
    }

    private String getRealTime(int time) {
        long tomorrow = System.currentTimeMillis() + 24 * 3600 * 1000;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        String prefix = simpleDateFormat.format(new Date(tomorrow));
        switch (time) {
            case 1:
                prefix += "10:00";
                break;
            case 2:
                prefix += "11:00";
                break;
            case 3:
                prefix += "12:00";
                break;
            case 4:
                prefix += "13:00";
                break;
            case 5:
                prefix += "14:00";
                break;
            case 6:
                prefix += "15:00";
                break;
            case 7:
                prefix += "16:00";
                break;
            case 8:
                prefix += "17:00";
                break;
            case 9:
                prefix += "18:00";
                break;
            case 10:
                prefix += "19:00";
                break;
            case 11:
                prefix += "20:00";
                break;
            case 12:
                prefix += "21:00";
                break;
            default:
        }
        return prefix;
    }
}
