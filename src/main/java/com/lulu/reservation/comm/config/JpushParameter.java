package com.lulu.reservation.comm.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/7/14 下午3:13
 * <p>
 * 类说明：
 *     极光推送的一些参数
 */
@Data
@Component
@ConfigurationProperties(prefix = "jpush")
public class JpushParameter {

    private String url;

    private String auth;

    private String tempId;
}
