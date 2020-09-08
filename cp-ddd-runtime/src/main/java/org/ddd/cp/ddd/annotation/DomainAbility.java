package org.ddd.cp.ddd.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 领域能力点，用于封装扩展点.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
public @interface DomainAbility {

    /**
     * The value may indicate a suggestion for a logical component name,
     * to be turned into a Spring bean in case of an autodetected component.
     *
     * @return the suggested component name, if any
     */
    @AliasFor(annotation = Component.class, attribute = "value") String value() default "";

    /**
     * 所属业务域.
     *
     * @return {@link Domain#code()}
     */
    String domain();

    /**
     * 能力名称.
     */
    String name() default "";
}
