package com.lulu.reservation.repository;

import com.lulu.reservation.domain.database.OrderRefund;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2018/8/22 下午4:33
 * <p>
 * 类说明：
 *     退款记录
 */

public interface OrderRefundRepository extends JpaRepository<OrderRefund, Long> {
}
