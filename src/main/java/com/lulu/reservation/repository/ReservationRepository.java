package com.lulu.reservation.repository;

import com.lulu.reservation.domain.database.Reservation;
import com.lulu.reservation.domain.database.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2018/8/22 下午1:35
 * <p>
 * 类说明：
 *     reservation repository
 */

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * 通过state查找约会
     * @param state 状态码
     * @return 约会列表
     */
    List<Reservation> findByState(Integer state);

}
