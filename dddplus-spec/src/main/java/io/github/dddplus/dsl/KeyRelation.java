/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.dsl;

import java.lang.annotation.*;

/**
 * 实体间关系.
 *
 * <p>使用时，单向标注即可，以主动方视角进行标注.</p>
 * <p>因此，不会有{@code BelongTo}、{@code ManyToOne}类型.</p>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Documented
@Repeatable(KeyRelations.class)
public @interface KeyRelation {

    /**
     * 跟哪个领域对象发生关系.
     */
    Class whom();

    /**
     * 发生什么关系.
     */
    Type type();

    String remark() default "";

    enum Type {
        /**
         * 非此即彼.
         */
        Union,
        HasOne,
        HasMany,

        @Deprecated
        Many2Many,

        /**
         * 特定场景对象.
         */
        Contextual,

        /**
         * MQ/Event等单向[异步]通知由谁发起.
         */
        NotifiedBy,

        /**
         * 当前对象来自于{@link #whom()}.
         */
        From,

        Extends,
        Implements

    }
}
