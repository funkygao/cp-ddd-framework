package org.x.cp.ddd.annotation;

import org.x.cp.ddd.model.IDomainStep;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * 领域活动步骤，它是一个业务抽象.
 * <p>
 * <p>一个步骤属于一个领域活动，用于被编排</p>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Service
public @interface Step {
    /**
     * The value may indicate a suggestion for a logical component name,
     * to be turned into a Spring bean in case of an autodetected component.
     *
     * @return the suggested component name, if any
     */
    @AliasFor(annotation = Service.class, attribute = "value") String value() default "";

    /**
     * 该步骤的名称.
     */
    String name() default "";

    /**
     * 该步骤所属分组.
     * <p>
     * <p>有的步骤非常大，例如：订单商品校验，涉及非常多的逻辑</p>
     * <p>这时候可以把它拆成多个步骤，但统一到“商品校验”分组里</p>
     * <p>分组，可以理解为标签：tag</p>
     */
    String[] groups() default {};

    /**
     * 是否可见，可以被编排.
     * <p>
     * <p>不可见的活动步骤，是builtin steps</p>
     */
    boolean visible() default true;

    /**
     * 该步骤依赖哪些其他步骤.
     * <p>
     * <p>即，被依赖的步骤先执行，才能执行本步骤</p>
     */
    Class<? extends IDomainStep>[] dependsOn() default {};
}
