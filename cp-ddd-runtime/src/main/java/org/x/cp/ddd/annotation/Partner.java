package org.x.cp.ddd.annotation;

import org.x.cp.ddd.model.IPartnerResolver;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 前台垂直业务实例，用于定位扩展点实例.
 * <p>
 * <p>垂直业务是不会叠加的，而是互斥的</p>
 * <p>每个垂直业务实例需要提供垂直业务解析器，来判断该业务是否属于自己</p>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
public @interface Partner {

    /**
     * 前台垂直业务编号.
     */
    String code();

    /**
     * 前台垂直业务名称.
     */
    String name();

    /**
     * 垂直业务解析器.
     */
    Class<? extends IPartnerResolver> resolverClass();
}
