package org.x.cp.ddd.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 业务域，领域.
 * <p>
 * <p>分为1个核心域和多个支撑域.</p>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
public @interface Domain {

    /**
     * 业务域编号.
     */
    String code();

    /**
     * 业务域名称.
     */
    String name();
}
