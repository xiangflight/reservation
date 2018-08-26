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

    private Integer time;

    private Integer area;

    private Integer save;

    public static MatchRequest newInstance() {
        return new MatchRequest();
    }

    @Override
    public String toString() {
        return "MatchRequest{" +
                "openid='" + openid + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", weight=" + weight +
                ", education=" + education +
                ", earn=" + earn +
                ", time=" + time +
                ", area=" + area +
                ", save=" + save +
                '}';
    }
}
