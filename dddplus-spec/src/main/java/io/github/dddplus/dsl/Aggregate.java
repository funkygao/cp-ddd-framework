/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.dsl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 聚合/问题域，非DDD项目可以理解为模块.
 *
 * <p>Applied on {@code package-info.java}.</p>
 * <p>Example:</p>
 * <pre>
 * {@code
 *
 * ℗Aggregate(name = "Foo", root = Foo.class)
 * package io.github.dddplus.domain.foo;
 *
 * import io.github.dddplus.dsl.Aggregate;
 * }
 * </pre>
 */
@Target(ElementType.PACKAGE)
@Retention(RetentionPolicy.SOURCE)
public @interface Aggregate {

    /**
     * Name of the aggregate: the business model context.
     */
    String name();

    /**
     * {@link io.github.dddplus.model.IAggregateRoot}.
     */
    Class[] root() default {};

    /**
     * 目前的模块划分是否存在问题.
     */
    boolean problematical() default false;
}
