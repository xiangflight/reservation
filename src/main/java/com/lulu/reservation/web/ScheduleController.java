package com.lulu.reservation.web;

import com.lulu.reservation.domain.database.Reservation;
import com.lulu.reservation.domain.database.User;
import com.lulu.reservation.repository.ReservationRepository;
import com.lulu.reservation.repository.UserRepository;
import com.lulu.reservation.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2018/8/25 下午9:59
 * <p>
 * 类说明：
 *     定时器
 */
@Component
@Slf4j
public class ScheduleController {

    private final ReservationRepository reservationRepository;

    private final UserRepository userRepository;

    @Autowired
    public ScheduleController(ReservationRepository reservationRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
    }


    @Transactional(rollbackFor = Exception.class)
    @Scheduled(cron = "0 0/1 * * * ?")
    public void restoreUnReservedState() {
        LocalDateTime localDateTime = LocalDateTime.now();
        log.info("executed time: {}", localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        final List<Reservation> reservations = reservationRepository.findByState(1);
        Long now = System.currentTimeMillis();
        log.info("current timestamp is {}", now);
        for (Reservation reservation : reservations) {
            String reserveTime = reservation.getReserveTime();
            Long reservedTimestamp = TimeUtil.date2TimeStamp(reserveTime, "yyyy年MM月dd日HH:mm");
            log.info("reservation timestamp is {}", reservedTimestamp);
            log.info("reservation diff: {}", now - reservedTimestamp);
            if (now - reservedTimestamp >= 1800 * 1000) {
                reservation.setState(0);
                reservationRepository.save(reservation);
                final Long other = reservation.getOther();
                final Long one = reservation.getOne();
                final User userOne = userRepository.findById(one).get();
                final User userOther = userRepository.findById(other).get();
                userOne.setState(0);
                userOther.setState(0);
                userRepository.save(userOne);
                userRepository.save(userOther);
            }
        }
    }

}
