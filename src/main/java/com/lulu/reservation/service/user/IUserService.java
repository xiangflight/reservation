package com.lulu.reservation.service.user;

import com.lulu.reservation.domain.request.BaseInfoRequest;
import com.lulu.reservation.domain.request.LoginRequest;
import com.lulu.reservation.domain.request.MoreInfoRequest;
import com.lulu.reservation.domain.request.ReservationRequest;
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

    /**
     * 获取用户信息接口
     * @param openId 用户openId
     * @return Resp
     */
    Resp info(String openId);

    /**
     * 上传用户基本信息
     * @param baseInfoRequest 用户基本信息
     * @return Resp
     */
    Resp baseInfo(BaseInfoRequest baseInfoRequest);

    /**
     * 上传用户附加信息
     * @param moreInfoRequest 用户附加信息
     * @return Resp
     */
    Resp moreInfo(MoreInfoRequest moreInfoRequest);

    /**
     * 获取用户约会信息
     * @param reservationRequest 用户约会信息
     * @return Resp
     */
    Resp reservationInfo(ReservationRequest reservationRequest);

}
