package com.lulu.reservation.domain.request;

import com.lulu.reservation.comm.algo.MatchCondition;
import lombok.Data;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/8/2 下午6:20
 * <p>
 * 类说明：
 *     匹配用户
 */
@Data
public class MatchRequest {

    private String openid;

    private Integer age;

    private Integer height;

    private Integer weight;

    private Integer education;

    private Integer earn;

    public static MatchRequest newInstance() {
        return new MatchRequest();
    }
}
