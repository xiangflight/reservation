package com.lulu.reservation.comm.algo;

import com.lulu.reservation.domain.database.User;

import java.util.HashSet;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2018/8/14 下午6:50
 * <p>
 * 类说明：
 *     匹配池
 */
public class MatchPool {

    private HashSet<User> userPool;

    private static class MatchPoolHelper {
        private static final MatchPool INSTANCE = new MatchPool();
    }

    public static MatchPool getInstance() {
        return MatchPoolHelper.INSTANCE;
    }

    /**
     * 将用户加入匹配池
     * @param user 用户
     */
    public void addUser(User user) {
        userPool.add(user);
    }

    /**
     * 将用户从匹配池中删除
     * @param user 用户
     */
    public void removeUser(User user) {
        userPool.remove(user);
    }

    public HashSet<User> getUserPool() {
        return userPool;
    }

}
