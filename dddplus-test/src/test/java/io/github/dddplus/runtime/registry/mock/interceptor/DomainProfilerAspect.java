package io.github.dddplus.runtime.registry.mock.interceptor;

import io.github.dddplus.runtime.BaseRouter;
import io.github.dddplus.step.IDomainStep;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class DomainProfilerAspect {

    @Around("@annotation(domainProfiler)")
    public Object around(ProceedingJoinPoint pjp, DomainProfiler domainProfiler) throws Throwable {
        Object target = pjp.getTarget(); // 被代理对象
        if (target instanceof IDomainStep) {
            IDomainStep step = (IDomainStep) target;
            log.info("AROUND step:{}.{}", step.activityCode(), step.stepCode());
        }

        if (target instanceof BaseRouter) {

        }

        return pjp.proceed();
    }
}
