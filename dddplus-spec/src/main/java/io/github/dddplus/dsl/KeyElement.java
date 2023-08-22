/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.dsl;

import java.lang.annotation.*;

/**
 * 业务对象的核心属性：业务方可感知的最小完备集.
 *
 * <p>排除为了(查询，报表)等场景而冗余的宽表逻辑字段.</p>
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
public @interface KeyElement {

    /**
     * 该属性属于哪些分类.
     */
    Type[] types() default Type.Structural;

    /**
     * 该属性名称在逆向建模时被修正为哪一个统一语言名称.
     *
     * <p>通过该机制，可以表达把多要素组合.</p>
     * <p>例如：{@code name = "(bizType, bizNo)"}.</p>
     * <p>这样，可以解决Java Annotation只能应用在单一字段的局限性.</p>
     */
    String name() default "";

    /**
     * 使用javadoc作为属性名称.
     */
    boolean byJavadoc() default false;

    /**
     * 该属性的名称是否根据类型来命名: 默认byName.
     *
     * <p>{@code String sku}，这样的属性是不能按类型来命名的: 无区分度</p>
     * <p>{@code OrderLine orderLine}，这样的属性适合{@code byType = true}</p>
     */
    boolean byType() default false;

    /**
     * 补充说明.
     */
    String remark() default "";

    /**
     * 使用源代码的Javadoc作为补充说明.
     */
    boolean remarkFromJavadoc() default false;

    /**
     * 业务属性的分类.
     *
     * <p>分类，容易让我们更接近问题本质：分类是人类最重要的认知.</p>
     */
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
         * 可能有问题的，是模型债.
         *
         * <p>在标注过程中发现的非领域概念被放在了领域模型.</p>
         * <ul>常见原因：
         * <li>为了数据库查询/reporting而产生的Entity内字段冗余，但与领域逻辑无关</li>
         * <li>Entity在处理请求过程中产生的中间结果，需要个地方保存</li>
         * <li>过早设计：想当然的需求，却无实际落地场景，产生僵尸(数据，逻辑)</li>
         * <li>直接把Entity等同于DTO，没有做领域层抽象，自然就不会有实质意义上的隔离和分层</li>
         * </ul>
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

        /**
         * 预留的扩展.
         */
        Reserved
    }
}
