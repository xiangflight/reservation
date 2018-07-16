package com.lulu.reservation.service.user;

import com.lulu.reservation.domain.request.LoginRequest;
import com.lulu.reservation.domain.response.Resp;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/7/16 下午8:03
 * <p>
 * 类说明：
 *     用户服务层
 */

public interface IUserService {

    /**
     * 登录接口
     * @param loginRequest 登录请求
     * @return result
     */
    Resp login(LoginRequest loginRequest);

}
