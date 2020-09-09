/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.ddd.cp.ddd.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 前台垂直业务实例，用于定位扩展点实例.
 * <p>
 * <p>垂直业务是不会叠加的，而是互斥的</p>
 * <p>每个垂直业务实例需要提供垂直业务解析器，来判断该业务是否属于自己</p>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
public @interface Partner {

    /**
     * 前台垂直业务编号.
     */
    String code();

    /**
     * 前台垂直业务名称.
     */
    String name();
}
