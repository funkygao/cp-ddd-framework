/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.dsl;

import java.lang.annotation.*;

/**
 * 业务对象的关键属性，揭示问题本质.
 *
 * <p>业务方可感知的概念.</p>
 * <p>这是通过数据来抽象行为的方法，参考<a href="http://c2.com/doc/oopsla89/paper.html">CRC</a>头脑风暴建模方法.</p>
 * <p>图表很有用，但它们不是模型，只是模型的不同视图：Model vs Views of the Model.</p>
 * <p>域专家不会根据屏幕或菜单项上的字段描述新的用户故事，而是讨论域对象所需的基础属性或行为.</p>
 * <p>Example:</p>
 * <pre>
 * {@code
 *
 * class Order {
 *     ℗KeyElement(type = KeyElement.Type.Quantity, name = "totalPrice")
 *     private BigDecimal price;
 * }
 * }
 * </pre>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
@Documented
@Inherited
public @interface KeyElement {

    /**
     * 该要素的归类.
     */
    Type[] types();

    /**
     * 该属性名称在逆向建模时被修正为哪一个统一语言名称.
     *
     * <p>通过该机制，可以表达把多要素组合.</p>
     * <p>例如：{@code name = "(bizType, bizNo)"}.</p>
     * <p>这样，可以解决Java Annotation只能应用在单一字段的局限性.</p>
     */
    String name() default "";

    /**
     * 补充说明.
     */
    String remark() default "";

    enum Type {

        /**
         * 结构类，关键构成类.
         */
        Structural,

        /**
         * 业务对象间的引用类.
         *
         * <p>Evidence.</p>
         */
        Referential,

        /**
         * 业务对象的生命周期类.
         */
        Lifecycle,

        /**
         * 生产运营类，也可以表达业务变化类和业务规则类.
         */
        Operational,

        /**
         * 可能有问题的.
         */
        Problematical,

        //============

        /**
         * 特定场景相关.
         */
        Contextual,

        /**
         * data collection unit.
         */
        DCU,

        /**
         * 传播类.
         *
         * <p>例如：后续流程使用该属性，为了保证数据项获取顺畅，需要本模型进行手递手传播.</p>
         */
        Propagational,

        /**
         * 数量类.
         */
        Quantity,

        /**
         * 计费要素类.
         */
        Billing,

        /**
         * 运营考核类.
         */
        KPI,

        /**
         * 位置，场.
         */
        Location,
    }
}
