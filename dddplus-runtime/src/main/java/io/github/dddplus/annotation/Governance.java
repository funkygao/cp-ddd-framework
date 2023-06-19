/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.annotation;

import java.lang.annotation.*;

/**
 * 公共方法粒度的服务治理，基于AOP.
 *
 * <p>目前只能应用在{@code BaseRouter}子类的public方法上</p>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Governance {
    /**
     * 是否对被拦截的方法调用次数、可用率、性能启用分析监控.
     *
     * @return true if yes
     */
    boolean profiler() default true;
}
