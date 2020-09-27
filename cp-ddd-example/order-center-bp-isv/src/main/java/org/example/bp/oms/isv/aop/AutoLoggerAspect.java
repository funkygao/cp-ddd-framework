package org.example.bp.oms.isv.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.InitializingBean;

@Aspect
@Slf4j
public class AutoLoggerAspect implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("注册 Spring lifecycle ok");
    }

    @Around("@annotation(autoLogger)")
    public Object around(ProceedingJoinPoint pjp, AutoLogger autoLogger) throws Throwable {
        Signature signature = pjp.getSignature();
        Object[] args = pjp.getArgs();
        log.info("{}.{} 入参:{}", signature.getDeclaringTypeName(), signature.getName(), args);
        try {
            return pjp.proceed();
        } catch (Throwable cause) {
            log.error("{}.{} error", signature.getDeclaringTypeName(), signature.getName(), cause);
            throw cause;
        }
    }
}
