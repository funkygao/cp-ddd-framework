/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 业务域.
 * <p>
 * <p>一个BC(Bounded Context)通常由1个核心域和多个支撑域组成.</p>
 * <p>核心域是业务的入口和出口，负责驱动业务逻辑的执行</p>
 * <p>支撑域为核心域提供必要的支撑，是某个功能模块的自治体系</p>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
@Deprecated
public @interface Domain {

    /**
     * 业务域编号.
     */
    String code();

    /**
     * 业务域名称.
     */
    String name();
}
