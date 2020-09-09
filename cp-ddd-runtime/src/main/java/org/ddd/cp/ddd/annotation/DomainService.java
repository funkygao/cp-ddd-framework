/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.ddd.cp.ddd.annotation;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * 领域服务.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Service
public @interface DomainService {

    /**
     * 所属业务域.
     *
     * @return {@link Domain#code()}
     */
    String domain();
}
