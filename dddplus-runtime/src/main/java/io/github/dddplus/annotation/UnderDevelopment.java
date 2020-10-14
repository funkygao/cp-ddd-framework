/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.annotation;

import java.lang.annotation.*;

/**
 * 正在积极开发中，但还未经过严格测试，不要在生产环境使用！</p>
 * <p>测试证明后，相应的代码会将这个注解解除.</p>
 */
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface UnderDevelopment {
}
