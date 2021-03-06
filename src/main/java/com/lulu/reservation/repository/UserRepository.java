package com.lulu.reservation.repository;

import com.lulu.reservation.domain.database.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/7/15 下午9:16
 * <p>
 * 类说明：
 *     用户Repository
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 通过openid查找用户id
     * @param openId openId
     * @return 用户id
     */
    User findByMpOpenid(String openId);

    /**
     * 通过state查找用户
     * @param state 状态码
     * @return 用户列表
     */
    List<User> findByState(Integer state);

}
