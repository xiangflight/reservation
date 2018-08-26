package com.lulu.reservation.repository;

import com.lulu.reservation.domain.database.OrderReservation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2018/8/18 下午4:28
 * <p>
 * 类说明：
 *     订单Repository
 */

public interface OrderRepository extends JpaRepository<OrderReservation, Long> {
}
