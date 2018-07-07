package com.lulu.reservation.domain.response;

import lombok.Data;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/7/7 下午5:02
 * <p>
 * 类说明：
 *     返回类
 */
@Data
public class Resp<T> {

    private Integer code;

    private String message;

    private T data;
}
