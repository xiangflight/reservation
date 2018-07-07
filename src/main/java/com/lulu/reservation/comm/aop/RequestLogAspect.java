package com.lulu.reservation.comm.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/22 下午8:29
 * <p>
 * 类说明：
 *     采用Spring AOP方式记录请求日志
 */
@Aspect
@Component
@Slf4j
public class RequestLogAspect {

    @Pointcut("execution(public * com.lulu.reservation.web.*.*(..))")
    public void appRequestLog() {}

    @Before("appRequestLog()")
    public void deBefore(JoinPoint joinPoint) {
        try {
            // 接收到请求，记录请求内容
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String path = joinPoint.getSignature().getDeclaringTypeName();
            String methodName = joinPoint.getSignature().getName();
            String uri = request.getRequestURI();
            String remoteAddr = getIpAddr(request);
            String sessionId = request.getSession().getId();
            String httpMethod = request.getMethod();
            Map<String, String[]> reqMap = request.getParameterMap();
            StringJoiner joiner = new StringJoiner(",");
            reqMap.forEach((k,v) -> {
                if(v.length != 1) {
                    joiner.add(k + ":" + Arrays.toString(v));
                }else {
                    joiner.add(k + ":" + v[0]);
                }
            });
            log.info("uri = {}, httpMethod = {}, sessionId = {}, remoteIp = {}",
                    uri, httpMethod, sessionId, remoteAddr);
        } catch (Exception e) {
            log.error("*******操作请求日志记录失败doBefore()*******", e);
        }
    }

    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
