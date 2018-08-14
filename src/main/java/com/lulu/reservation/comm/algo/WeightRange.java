package com.lulu.reservation.comm.algo;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2018/8/14 下午8:14
 * <p>
 * 类说明：
 *     体重范围
 */

public class WeightRange {

    private Float start;
    private Float end;

    public WeightRange(Float start, Float end) {
        this.start = start;
        this.end = end;
    }

    public boolean isBetween(Float weight) {
        return weight.compareTo(start) >= 0 && weight.compareTo(end) <= 0;
    }
}
