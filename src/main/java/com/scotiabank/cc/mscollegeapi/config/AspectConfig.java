package com.scotiabank.cc.mscollegeapi.config;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Aspect
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AspectConfig {

    @Pointcut("execution(public * com.scotiabank.cc.mscollegeapi.service..*.*(..))")
    public void logServiceMethod() { }

    @Pointcut("execution(public * com.scotiabank.cc.mscollegeapi.repository..*.*(..))")
    public void logRepositoryMethod() { }

    @Pointcut("execution(public * com.scotiabank.cc.mscollegeapi.controller..*.*(..))")
    public void logControllerMethod() { }
}
