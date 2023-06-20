/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.dsl;

import java.lang.annotation.*;

/**
 * 关键业务用例.
 *
 * <p>业务的核心入口，常用于：Controller/ApplicationService/MQ Consumer.</p>
 * <p>Example:</p>
 * <pre>
 * {@code
 * class OrderController {
 *     ℗KeyUsecase(in = {"orderNo", "containerNo"})
 *     ℗PostMapping("a/b")
 *     public Response doSth(Request request) {}
 * }
 * }
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
@Documented
@Inherited
public @interface KeyUsecase {

    /**
     * 该属性名称在逆向建模时被修正为哪一个统一语言名称.
     */
    String name() default "";

    /**
     * 补充说明.
     */
    String remark() default "";

    /**
     * 关键的入参.
     *
     * <p>虽然可以自动分析方法的入参，但这里提供了修正的机会：报文里关键信息需要人工标注.</p>
     */
    String[] in() default {};

    /**
     * 关键的出参.
     *
     * <p>虽然可以自动分析方法的出参，但这里提供了修正的机会：报文里关键信息需要人工标注.</p>
     */
    String[] out() default {};
}
