/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.dsl;

import java.lang.annotation.*;

/**
 * 业务对象的关键行为，只隶属于当前对象.
 *
 * <p>业务方可感知的行为.</p>
 * <p>vs {@link KeyFlow}：后者可以通过{@link KeyFlow#actor()}修正绑定到某个业务对象，而{@link KeyBehavior}只能标注到当前业务对象.</p>
 * <p>Example:</p>
 * <pre>
 * {@code
 *
 * class ShipmentOrder {
 *     ℗KeyBehavior
 *     void ship(Operator operator) {}
 * }
 * }
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface KeyBehavior {

    /**
     * 该属性名称在逆向建模时被修正为哪一个统一语言名称.
     *
     * <p>如果不指定，则使用AST分析得到方法名称.</p>
     */
    String name() default "";

    /**
     * 补充说明.
     */
    String remark() default "";

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
     * 该行为会产生哪些领域事件.
     */
    Class[] produceEvent() default {};

    /**
     * 该行为执行过程是异步的.
     *
     * <p>这意味着内部状态变化可能不实时，可能最终一致.</p>
     */
    boolean async() default false;

    /**
     * 该行为是否是抽象行为，由子类实现.
     */
    boolean abstracted() default false;
}
