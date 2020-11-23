/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.annotation;

import io.github.dddplus.ext.IDomainExtension;
import io.github.dddplus.ext.IExtPolicy;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 扩展点定位策略，需要实现{@link IExtPolicy}接口.
 *
 * <p>一个扩展点定位策略，只能对应一个扩展点类型：从该扩展点的多个实例中选取一个.</p>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
public @interface Policy {

    /**
     * 该扩展点定位策略对应的扩展点类型.
     */
    Class<? extends IDomainExtension> extClazz();
}
