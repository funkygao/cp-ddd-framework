/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.annotation;

import io.github.dddplus.ext.IPolicy;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 单一扩展点路由策略，注解在{@link IPolicy}之上.
 * <p>
 * <p>是一个扩展点的局部(非全局)路由的方便机制</p>
 * <ul>vs {@link Pattern}
 * <li>{@link Policy}只能对应一个扩展点：从该扩展点的多个实例中选取一个；{@link Pattern}往往对应多个扩展点行为</li>
 * <li>{@link Policy}只关注局部；{@link Pattern}关注全局，至少跨use case</li>
 * <li>{@link Policy}的扩展点路由，不必定义{@link Router}实现类，开发工作量少</li>
 * </ul>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
public @interface Policy {
}
