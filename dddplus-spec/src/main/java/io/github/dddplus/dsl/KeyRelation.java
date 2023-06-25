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
 * <p>使用时，单向标注即可</p>
 * <p>Example:</p>
 * <pre>
 * {@code
 *
 * ℗KeyRelation(whom = ShipmentOrderItem.class, type = KeyRelation.Type.HasMany)
 * ℗KeyRelation(whom = Pack.class, type = KeyRelation.Type.HasMany, remark = "一个订单可能多个包裹")
 * class ShipmentOrder {}
 * class ShipmentOrderItem {}
 * }
 * </pre>
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
        BelongTo,

        /**
         * @deprecated 如果使用，容易导致生成的类图显示混乱.
         */
        @Deprecated
        Many2Many,

        /**
         * 特定场景对象.
         *
         * @see io.github.dddplus.model.BoundedDomainModel
         */
        Contextual,

        /**
         * 当前对象来自于{@link #whom()}.
         */
        From,

        Extends,
        Implements

    }
}
