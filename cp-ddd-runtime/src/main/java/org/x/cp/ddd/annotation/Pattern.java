package org.x.cp.ddd.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 业务模式，用于定位扩展点实例.
 * <p>
 * <p>横向视角把扩展点进行聚合，属于水平业务，可以叠加</p>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
public @interface Pattern {

    /**
     * 业务模式编号.
     */
    String code();

    /**
     * 业务模式名称.
     */
    String name();

    /**
     * 优先级，越小表示优先级越高.
     * <p>
     * <p>用于解决业务模式匹配的顺序问题</p>
     * <p>只应用于同一个扩展点在不同{@code Pattern}间的顺序问题，不同的扩展点间优先级不具备可比性</p>
     */
    int priority() default 1024;
}
