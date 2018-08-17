package com.lulu.reservation.comm.algo;

import lombok.Data;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2018/8/14 下午7:08
 * <p>
 * 类说明：
 *     匹配条件
 */
@Data
public class MatchCondition {

    private AgeRange ageRange;

    private HeightRange heightRange;

    private WeightRange weightRange;

    private int education;

    private int earn;

    private int time;

    private int district;

}
