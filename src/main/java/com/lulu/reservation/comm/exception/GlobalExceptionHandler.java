package com.lulu.reservation.comm.exception;

import com.lulu.reservation.comm.Constants;
import com.lulu.reservation.domain.response.Resp;
import com.lulu.reservation.util.RespUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/2/23 下午2:36
 * <p>
 * 类说明：
 * 统一异常处理
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Resp handleException(Exception e) throws Exception{
        if (e instanceof ParamException) {
            ParamException paramException = (ParamException) e;
            return RespUtil.errorResp(paramException.getCode(), paramException.getMessage());
        } else{
            log.error("[系统异常] {}", e);
            throw e;
        }
    }

}
