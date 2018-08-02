package com.lulu.reservation.web;

import com.lulu.reservation.domain.request.GetInfoRequest;
import com.lulu.reservation.domain.request.LoginRequest;
import com.lulu.reservation.domain.response.Resp;
import com.lulu.reservation.service.user.IUserService;
import com.lulu.reservation.util.RespUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
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
}