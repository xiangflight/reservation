package com.lulu.reservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/7/7 下午11:29
 * <p>
 * 类说明：
 *     Spring boot工程启动类
 */
@SpringBootApplication
public class ReservationApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ReservationApplication.class, args);
    }

    /**
     * 部署到Tomcat中必须添加SpringBootServletInitializer
     * @param builder builder
     * @return SpringApplicationBuilder obj
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ReservationApplication.class);
    }
}
