package com.team.updevic001.configuration.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    //Lazim olsa islederik ama heleki ehtiyac deyil
//
//    // Pointcut: Bizim istəyimizə uyğun metodları seçmək
//    @Pointcut("execution(* com.team.updevic001.services.impl.AdminServiceImpl.*(..))")
//    public void adminServiceMethods() {
//        // Burada xüsusi metodları seçirik
//    }
//
//    // Metodun başlanğıcında log mesajı
//    @Before("adminServiceMethods()")
//    public void logBefore(JoinPoint joinPoint) {
//        log.info("Starting method: {} with arguments: {}", joinPoint.getSignature().getName(), joinPoint.getArgs());
//    }
//
//    // Metodun sonunda log mesajı
//    @After("adminServiceMethods()")
//    public void logAfter(JoinPoint joinPoint) {
//        log.info("Method {} completed.", joinPoint.getSignature().getName());
//    }
}
