package org.cmc.curtaincall.domain.core;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.OptimisticLockingFailureException;

@Aspect
@Slf4j
@Configuration
public class OptimisticLockAspect {

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Around("@within(org.cmc.curtaincall.domain.core.OptimisticLock)")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        int i = 0;
        while (true) {
            try {
                return joinPoint.proceed();
            } catch (OptimisticLockingFailureException e) {
                if (i == 20) {
                    throw e;
                }
                Thread.sleep(50);
            }
            i++;
        }
    }
}