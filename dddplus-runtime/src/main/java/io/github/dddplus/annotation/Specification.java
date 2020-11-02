/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 业务约束规则，注解在{@link io.github.dddplus.specification.ISpecification}之上.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
public @interface Specification {

    /**
     * 业务约束名称.
     */
    String value();

    /**
     * 该业务约束规则所属标签.
     * <p>
     * <p>通过标签，对业务约束规则进行归类</p>
     */
    String[] tags() default {};

}
