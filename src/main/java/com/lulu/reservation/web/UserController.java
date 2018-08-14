package com.lulu.reservation.web;

import com.lulu.reservation.comm.Constants;
import com.lulu.reservation.comm.algo.*;
import com.lulu.reservation.domain.database.User;
import com.lulu.reservation.domain.request.*;
import com.lulu.reservation.domain.response.Resp;
import com.lulu.reservation.repository.UserRepository;
import com.lulu.reservation.service.user.IUserService;
import com.lulu.reservation.util.RespUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.util.HashSet;

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

    @Autowired
    public UserController(IUserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
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
        MatchPool.getInstance().addUser(curUser);
        // 从匹配的用户池中匹配10s后退出
        final Integer sex = curUser.getSex();
        int other = 0;
        if (sex == 1) {
            other = 2;
        } else {
            other = 1;
        }
        long now = System.currentTimeMillis();
        final HashSet<User> userPool = MatchPool.getInstance().getUserPool();
        while (true) {
            if (System.currentTimeMillis() - now >= 10 * 1000) {
                break;
            }
            for (User user: userPool) {
                if (user.getSex() != other) {
                    continue;
                }
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
                    // 找到匹配的人
                    // 匹配的两人分别是curUser和user
                    // 发送双方信息给对方，同时从匹配池中删除这两人
                    userPool.remove(curUser);
                    userPool.remove(user);
                    return RespUtil.successResp();
                }
            }
        }
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

}
