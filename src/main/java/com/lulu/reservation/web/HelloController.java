package com.lulu.reservation.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/7/6 下午6:24
 * <p>
 * 类说明：
 *     实验性Controller
 */
@RestController
public class HelloController {

    private static final String HELLO_WORLD_CONSTANT = "hello world";

    @RequestMapping("hello")
    public String hello() {
        return HELLO_WORLD_CONSTANT;
    }

}
