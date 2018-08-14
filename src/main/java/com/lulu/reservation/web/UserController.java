package com.lulu.reservation.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lulu.reservation.comm.Constants;
import com.lulu.reservation.comm.algo.*;
import com.lulu.reservation.comm.config.WechatParameter;
import com.lulu.reservation.domain.database.User;
import com.lulu.reservation.domain.request.*;
import com.lulu.reservation.domain.response.Resp;
import com.lulu.reservation.repository.UserRepository;
import com.lulu.reservation.service.user.IUserService;
import com.lulu.reservation.util.RespUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/7/16 下午8:01
 * <p>
 * 类说明：
 * ${description}
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    private final IUserService userService;

    private final UserRepository userRepository;

    private final WechatParameter wechatParameter;

    @Autowired
    public UserController(IUserService userService, UserRepository userRepository, WechatParameter wechatParameter) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.wechatParameter = wechatParameter;
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
    public Resp match(@RequestBody MatchRequest matchRequest) {
        final String openid = matchRequest.getOpenid();
        MatchCondition matchCondition = constructMatchCondition(matchRequest);
        // 加入匹配池中
        User curUser = userRepository.findByMpOpenid(openid);
        log.info("curUser is {}", curUser.getId());
        final Integer sex = curUser.getSex();
        log.info("current sex：{}", sex);
        int other = 0;
        if (sex == 1) {
            other = 2;
        } else {
            other = 1;
        }
        long now = System.currentTimeMillis();
        log.info("now is {}", now);
        curUser.setState(1);
        userRepository.save(curUser);
        log.info("active current user");
        while (true) {
            log.info("in while loop");
            // check state
            curUser = userRepository.findByMpOpenid(openid);
            if (curUser.getState() != 1) {
                // exit loop
                return RespUtil.successResp();
            }
            if (System.currentTimeMillis() - now >= 10 * 1000) {
                log.info("division {}", System.currentTimeMillis() - now);
                break;
            }
            List<User> users = userRepository.findByState(1);
            for (User user: users) {
                if (user.getSex() != other) {
                    continue;
                }
                log.info("进到匹配池中匹配异性...");
                final Integer age = user.getAge();
                final Float tall = user.getTall();
                final Float weight = user.getWeight();
                final Integer education = user.getEducation();
                final Integer earn = user.getEarn();
                if (matchCondition.getAgeRange().isBetween(age) &&
                    matchCondition.getHeightRange().isBetween(tall) &&
                    matchCondition.getWeightRange().isBetween(weight) &&
                    matchCondition.getEducation() == education &&
                    matchCondition.getEarn() == earn) {
                    sendTemplateMessage(curUser);
                    sendTemplateMessage(user);
                    user.setState(0);
                    curUser.setState(0);
                    userRepository.save(user);
                    userRepository.save(curUser);
                    return RespUtil.successResp();
                }
            }
        }
        log.info("match failure");
        return RespUtil.errorResp(Constants.StatusErr.INVALID);
    }

    private MatchCondition constructMatchCondition(MatchRequest matchRequest) {
        MatchCondition matchCondition = new MatchCondition();
        final Integer age = matchRequest.getAge();
        AgeRange ageRange = null;
        switch (age) {
            case 1:
                ageRange = new AgeRange(20, 25);
                break;
            case 2:
                ageRange = new AgeRange(25, 30);
                break;
            case 3:
                ageRange = new AgeRange(30, 35);
                break;
            case 4:
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
                heightRange = new HeightRange(165.0f, 170.0f);
                break;
            case 2:
                heightRange = new HeightRange(170.0f, 175.0f);
                break;
            default:
                break;
        }
        matchCondition.setHeightRange(heightRange);
        final Integer weight = matchRequest.getWeight();
        WeightRange weightRange = null;
        switch (weight) {
            case 1:
                weightRange = new WeightRange(40.0f, 50.0f);
                break;
            case 2:
                weightRange = new WeightRange(50.0f, 55.0f);
                break;
            default:
                break;
        }
        matchCondition.setWeightRange(weightRange);
        matchCondition.setEducation(matchRequest.getEducation());
        matchCondition.setEarn(matchRequest.getEarn());
        return matchCondition;
    }

    /**
     * 推送模板消息
     * @param user 向用户推送模板消息
     */
    private void sendTemplateMessage(User user) {
        // get token
        RestTemplate restTemplate = new RestTemplate();
        String getTokenUrl = String.format(Constants.WX_MP_GET_ACCESS_TOKEN_URL, wechatParameter.getId(), wechatParameter.getSecret());
        String response = restTemplate.getForObject(getTokenUrl, String.class);
        log.info("token response: {}", response);
        JSONObject accessTokenJson = JSON.parseObject(response);
        String accessToken = accessTokenJson.getString("access_token");
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken;
        final String openid = user.getMpOpenid();
        String templateId = "8ZXpH0-G0W7PleDkVJ8d16tAq3nsfGpkZgrcovGBzZU";
        String requestBody = "{\n" +
                "           \"touser\":\"" + openid + "\",\n" +
                "           \"template_id\":\""+ templateId + "\",\n" +
                "           \"url\":\"http://zynei.com/reservation/wx/auth\",  \n" +
                "           \"data\":{\n" +
                "                   \"first\": {\n" +
                "                       \"value\":\"恭喜你匹配成功！\",\n" +
                "                       \"color\":\"#173177\"\n" +
                "                   },\n" +
                "                   \"keyword1\":{\n" +
                "                       \"value\":\"明天\",\n" +
                "                       \"color\":\"#173177\"\n" +
                "                   },\n" +
                "                   \"keyword2\": {\n" +
                "                       \"value\":\"后天\",\n" +
                "                       \"color\":\"#173177\"\n" +
                "                   },\n" +
                "                   \"remark\":{\n" +
                "                       \"value\":\"祝您愉快\",\n" +
                "                       \"color\":\"#173177\"\n" +
                "                   }\n" +
                "           }\n" +
                "       }";
        response = restTemplate.postForObject(url, requestBody, String.class);
        log.info("send template response: {}", response);
    }

}
