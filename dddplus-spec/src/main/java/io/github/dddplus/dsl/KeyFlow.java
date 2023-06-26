/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.dsl;

import java.lang.annotation.*;

/**
 * 关键业务逻辑流程.
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
@Documented
@Inherited
public @interface KeyFlow {

    /**
     * 业务规则约束对应的类.
     */
    Class[] rules() default {};

    /**
     * 行为主体对应的类.
     */
    Class[] actor() default {};

    /**
     * 行为模式，即业务场景变化点.
     */
    String[] modes() default {};

    /**
     * 行为模式对应的类，通常是业务字典类.
     *
     * <p>它与{@link #modes()}合并，共同定义行为模式.</p>
     */
    Class[] modeClass() default {};

    /**
     * 该行为会产生哪些领域事件.
     */
    Class[] produceEvent() default {};

    /**
     * 关键的入参.
     *
     * <p>虽然可以自动分析方法的入参，但这里提供了修正的机会.</p>
     */
    String[] args() default {};

    /**
     * 该属性名称在逆向建模时被修正为哪一个统一语言名称.
     */
    String name() default "";

    /**
     * 补充说明.
     */
    String remark() default "";
}
