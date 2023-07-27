/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.dsl;

import java.lang.annotation.*;

/**
 * 业务对象间关系.
 *
 * <p>JDK8+才支持在一处多次标注</p>
 * <p>Example:</p>
 * <pre>
 * {@code
 *
 * ℗KeyRelation(whom = ShipmentOrderItem.class, type = KeyRelation.Type.HasMany)
 * ℗KeyRelation(whom = Pack.class, type = KeyRelation.Type.HasMany, remark = "一个订单可能多个包裹")
 * class ShipmentOrder {}
 * class ShipmentOrderItem {}
 * class Pack {}
 * }
 * </pre>
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
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

    /**
     * 这种关系是否某些特定场景才发生.
     */
    boolean contextual() default false;

    String remark() default "";

    /**
     * 业务对象间关系的分类.
     */
    enum Type {
        /**
         * 非此即彼.
         */
        Union,
        HasOne,
        HasMany,
        BelongTo,
        Associate,

        /**
         * 面向对象构想的信息结构是树形，而关系模型是集合，它们有一个天然的鸿沟.
         *
         * <p>模型之间本质上没有多对多关系，如果有，说明存在一个隐含的成员关系，这个关系没有被充分的分析出来.</p>
         * @deprecated
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
        Implements;

        public static boolean match(String typeStr) {
            boolean matched = false;
            for (Type type : values()) {
                if (type.toString().equals(typeStr)) {
                    matched = true;
                    break;
                }
            }

            return matched;
        }

    }
}
