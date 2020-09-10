/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.ddd.cp.ddd.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 业务模式，用于定位扩展点实例.
 * <p>
 * <p>横向视角把扩展点进行聚合，属于水平业务，可以叠加</p>
 * <p>所谓横向视角，相当于AOP里的Aspect</p>
 * <p>每个业务模式需要实现{@link org.ddd.cp.ddd.ext.IIdentityResolver}，来判断该业务是否属于自己</p>
 * <ul>如何理解{@code Pattern}与{@code IDomainExtension}？
 * <li>{@code Pattern}，相当于把散落在各处的某个业务逻辑{@code if} 判断条件，收敛到{@code Pattern}里，使得这些业务判断显式化，有形化，并有了个名字</li>
 * <li>扩展点，相当于把if后面的code block显式化，有形化</li>
 * </ul>
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
    String name();

    /**
     * 优先级，越小表示优先级越高.
     * <p>
     * <p>用于解决业务模式匹配的顺序问题</p>
     * <p>只应用于同一个扩展点在不同{@code Pattern}间的顺序问题，不同的扩展点间优先级不具备可比性</p>
     */
    int priority() default 1024;
}
