package com.lulu.reservation.comm.algo;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2018/8/14 下午8:13
 * <p>
 * 类说明：
 *     身高范围
 */

public class HeightRange {

    private Float start;
    private Float end;

    public HeightRange(Float start, Float end) {
        this.start = start;
        this.end = end;
    }

    public boolean isBetween(Float height) {
        return height.compareTo(start) >= 0 && height.compareTo(end) <= 0;
    }

}
