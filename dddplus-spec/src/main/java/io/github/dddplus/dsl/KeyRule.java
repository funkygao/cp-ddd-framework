/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.dsl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 关键规则，控制逻辑.
 *
 * <p>对于无入参且返回类型是Boolean/boolean的公共方法，无需标注，自动注册为{@link KeyRule}.</p>
 * <p>如果想修正，再手工标注.</p>
 * <p>Example:</p>
 * <pre>
 * {@code
 *
 * class ShipmentOrder {
 *     ℗KeyRule(refer = WaybillSourceEnum.class)
 *     public WaybillSourceEnum waybillPlatform() {}
 * }
 * }
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface KeyRule {

    /**
     * 该属性名称在逆向建模时被修正为哪一个统一语言名称.
     */
    String name() default "";

    String remark() default "";

    /**
     * 相关对象.
     */
    Class[] refer() default {};
}
