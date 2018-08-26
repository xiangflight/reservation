package com.lulu.reservation.comm.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/7/14 下午7:33
 * <p>
 * 类说明：
 *     微信公众号的一些参数
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatParameter {

    private String token;

    private String id;

    private String secret;

    private String mchid;

    private String key;
}
