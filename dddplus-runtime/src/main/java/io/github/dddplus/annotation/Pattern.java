/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.annotation;

import io.github.dddplus.runtime.BasePattern;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 业务模式身份，被标注的类可继承{@link BasePattern}模板方法类.
 * <p>
 * <p>中台架构的本质，是业务模式的复用</p>
 * <p>业务模式，全称：特定场景中的业务模式，可以简单理解为场景的模式化，场景(scenario)是问题，包括时间地点起因经过结果，{@link Pattern}是以场景为输入的解决方案</p>
 * <p>业务模式帮助我们理解业务本质，而且在业务场景下帮助我们做出业务决策，业务模式实际上是一组决策模式的集合</p>
 * <p>业务模式需要一种恰如其分的抽象：适度地泛化可以在不同的场景下复用，但又要避免在具体的场景下丧失它对于业务决策的指导意义</p>
 * <p>对于业务模式，最好的办法是从已经存在的业务模型中去提取：泛化，归一化，语义标准化</p>
 * <p>设计思想：模式的定义、模式的相关行为与实现分离</p>
 * <p>一个请求可能涉及多个{@link Pattern}的叠加，例如：按生产单出库的校验，可能涉及【预售模式】、【质押模式】等的叠加，都允许才能出库</p>
 * <ul>如何理解{@code Pattern}与{@code IDomainExtension}？
 * <li>{@code Pattern}，相当于把散落在各处的某个业务逻辑{@code if} 判断条件，收敛到{@code Pattern}类里，使得这些业务判断显式化，有形化，有了化身，并有了个名字(UL)</li>
 * <li>扩展点，相当于把{@code if} 后面的code block抽象并归一化，是一种细粒度的行为</li>
 * <li>一个{@link Pattern}可能对应多个扩展点，即设计思想里的【模式的定义与模式相关行为分离】，例如：预售模式行为可能涉及出库前校验、接单补全、生产计划等</li>
 * </ul>
 * <p>{@link Pattern}是InnerSource开发模式的有效手段：中台定义模式编码并通过{@link Pattern}统一定义路由，中台与BP共同在一个共享代码库实现{@link Pattern}对应的扩展点.</p>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
public @interface Pattern {

    /**
     * 业务模式编号.
     */
    String code();

    /**
     * 业务模式名称.
     */
    String name() default "";

    /**
     * 该业务模式的业务标签.
     */
    String[] tags() default {};

    /**
     * 优先级，越小表示优先级越高.
     * <p>
     * <p>用于解决业务模式匹配的顺序问题</p>
     * <p>只应用于同一个扩展点在不同{@code Pattern}间的顺序问题，不同的扩展点间优先级不具备可比性</p>
     */
    int priority() default 1024;
}
