/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.dsl;

import java.lang.annotation.*;

/**
 * 业务模型的关键行为.
 *
 * <p>业务方可感知的行为.</p>
 * <p>vs {@link KeyFlow}：后者可以通过{@link KeyFlow#actor()}修正绑定到某个业务对象，而{@link KeyBehavior}只能标注到当前业务对象.</p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
@Documented
@Inherited
public @interface KeyBehavior {

    /**
     * 该属性名称在逆向建模时被修正为哪一个统一语言名称.
     */
    String name() default "";

    /**
     * 补充说明.
     */
    String remark() default "";

    /**
     * 业务规则约束对应的类.
     */
    Class[] rules() default {};

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
     * 关键的入参.
     *
     * <p>虽然可以自动分析方法的入参，但这里提供了修正的机会.</p>
     */
    String[] args() default {};

    @Deprecated
    Class[] depends() default {};

}
