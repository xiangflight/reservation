package com.lulu.reservation.comm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/7/11 上午10:51
 * <p>
 * 类说明：
 *     WebMvc配置类
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * controller -> view
     * @param registry registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("/login");
        registry.addViewController("/index").setViewName("/index");
    }

    /**
     * 添加cors映射，解决跨域问题
     * @param registry registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("X-Requested-With", "Content-Type")
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .allowedOrigins("*")
                .maxAge(3600)
                .allowCredentials(true);
    }
}
