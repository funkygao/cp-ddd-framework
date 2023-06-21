/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.annotation;

import io.github.dddplus.model.IDomainService;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * 领域服务，注解在{@link IDomainService}之上.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Service
@Deprecated
public @interface DomainService {

    /**
     * 所属业务域.
     *
     * @return {@link Domain#code()}
     */
    String domain();
}
