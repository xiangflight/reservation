package com.lulu.reservation.util;

import com.lulu.reservation.comm.Constants;
import com.lulu.reservation.domain.response.Resp;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/7/7 下午5:08
 * <p>
 * 类说明：
 *     返回工具类
 */

public class RespUtil {

    public static <T> Resp<T> successResp(T data) {
        Resp<T> resp = new Resp<>();
        resp.setCode(Constants.ApiErr.SUCCESS.getCode());
        resp.setMessage(Constants.ApiErr.SUCCESS.getMsg());
        resp.setData(data);
        return resp;
    }

    public static Resp successResp() {
        return successResp(null);
    }

    @SuppressWarnings(value = "unchecked")
    public static Resp errorResp(Constants.StatusErr statusErr) {
        Resp resp = new Resp();
        resp.setCode(statusErr.getCode());
        resp.setMessage(statusErr.getMsg());
        resp.setData(null);
        return resp;
    }

    @SuppressWarnings(value = "unchecked")
    public static Resp errorResp(Integer code, String message) {
        Resp resp = new Resp();
        resp.setData(null);
        resp.setCode(code);
        resp.setMessage(message);
        return resp;
    }
}
