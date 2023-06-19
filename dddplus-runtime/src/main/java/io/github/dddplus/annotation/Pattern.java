/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.annotation;

import io.github.dddplus.ext.IDomainExtension;
import io.github.dddplus.runtime.BasePattern;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 业务模式的身份识别.
 * <p>
 * <p>全局业务模式，串联面状业务变化，识别出业务模式需要全局(至少是多环节)地对业务变化进行本质性思考，这些不同变化都可以归因到某个业务模式</p>
 * <p>设计思想：模式的定义、模式的相关行为与实现分离，背后是关注点分离，隔离变化</p>
 * <p>一个请求可能涉及多个{@link Pattern}的叠加，即业务身份重叠，例如：按生产单出库的校验，可能涉及【预售模式】、【质押模式】等的叠加，都允许才能出库</p>
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

    /**
     * 该{@code Pattern}类是否用于解析和识别业务模式.
     * <p>
     * <ul>{@code Pattern}特有的行为，不仅包括{@link IDomainExtension}，还可能有：
     * <li>Application Service</li>
     * <li>Domain Service</li>
     * <li>etc</li>
     * </ul>
     * <p>对于这类非业务模式解析和识别应用场景，需要设置{@link #asResolver()} ()}为{@code false}.</p>
     */
    boolean asResolver() default true;
}
