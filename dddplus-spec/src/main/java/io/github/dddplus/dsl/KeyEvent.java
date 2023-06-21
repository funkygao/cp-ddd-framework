/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.dsl;

import java.lang.annotation.*;

/**
 * 关键领域事件.
 *
 * <p>Example:</p>
 * <pre>
 * {@code
 *
 * ℗KeyEvent(when = "订单完全发货后")
 * class OrderShipped {}
 * }
 * </pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface KeyEvent {

    /**
     * 何时触发该事件产生.
     */
    String when() default "";

    /**
     * 被哪些主体发送出来.
     */
    Class[] by() default {};
}
