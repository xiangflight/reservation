package com.lulu.reservation.comm.algo;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2018/8/14 下午8:12
 * <p>
 * 类说明：
 *     年龄范围
 */

public class AgeRange {

    private int start;
    private int end;

    public AgeRange(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public boolean isBetween(int age) {
        return age >= start && age <= end;
    }

}
