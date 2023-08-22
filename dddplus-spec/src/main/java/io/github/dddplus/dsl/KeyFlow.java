/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.dsl;

import java.lang.annotation.*;

/**
 * 关键业务逻辑流程，也可以理解为{@code KeyService}的某个方法.
 *
 * <p>Example:</p>
 * <pre>
 * {@code
 *
 * class OrderAppService {
 *     ℗KeyFlow(remark = "统一接单")
 *     public void submitOrder(OrderDto dto) throws BizException {}
 * }
 * }
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface KeyFlow {

    /**
     * 该属性名称在逆向建模时被修正为哪一个统一语言名称.
     */
    String name() default "";

    /**
     * 关键的入参.
     *
     * <p>虽然可以自动分析方法的入参，但这里提供了修正的机会.</p>
     */
    String[] args() default {};

    /**
     * 是否拿方法签名的入参作为{@link #args()}.
     */
    boolean useRawArgs() default false;

    /**
     * 补充说明.
     */
    String remark() default "";

    /**
     * 行为主体对应的类.
     *
     * <p>重新分配职责的过程.</p>
     */
    Class[] actor() default {};

    /**
     * 该行为会产生哪些领域事件.
     *
     * 松散的业务逻辑链路还原的过程.
     */
    Class[] produceEvent() default {};

    /**
     * 该流程是否位于业务交互层.
     */
    boolean usecase() default false;

    /**
     * 该行为执行过程是异步的.
     *
     * <p>这意味着内部状态变化可能不实时，可能最终一致.</p>
     */
    boolean async() default false;

    /**
     * 该行为是否多态.
     *
     * <p>即一个方法有多个实现.</p>
     */
    boolean polymorphism() default false;
}
