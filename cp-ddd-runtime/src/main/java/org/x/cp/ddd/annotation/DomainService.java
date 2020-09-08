package org.x.cp.ddd.annotation;

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
