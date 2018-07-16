package com.lulu.reservation.repository;

import com.lulu.reservation.domain.database.Sms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/7/7 下午4:51
 * <p>
 * 类说明：
 *     短信Repository
 */
public interface SmsRepository extends JpaRepository<Sms, Long> {

    /**
     * 倒序第一个sms
     * @param phone phone
     * @return sms
     */
    Sms findFirstByPhoneOrderByIdDesc(String phone);
}
