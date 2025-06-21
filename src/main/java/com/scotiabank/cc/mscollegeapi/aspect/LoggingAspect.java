package com.scotiabank.cc.mscollegeapi.aspect;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Log4j2
@Aspect
@Component
public class LoggingAspect {

    @Around("com.scotiabank.cc.mscollegeapi.config.AspectConfig.logControllerMethod() || " +
            "com.scotiabank.cc.mscollegeapi.config.AspectConfig.logServiceMethod() || " +
            "com.scotiabank.cc.mscollegeapi.config.AspectConfig.logRepositoryMethod()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch watch = new StopWatch();
        watch.start();

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        try {
            Object result = joinPoint.proceed();
            watch.stop();

            log.info("[{}] {}.{} - {}ms", Thread.currentThread().getName(), className, methodName,
                    watch.getTotalTimeMillis());

            return result;
        } catch (Exception e) {
            watch.stop();
            log.error("[{}] {}.{} - {}ms - Error: {}", Thread.currentThread().getName(), className, methodName,
                    watch.getTotalTimeMillis(), e.getMessage());
            throw e;
        }
    }
}