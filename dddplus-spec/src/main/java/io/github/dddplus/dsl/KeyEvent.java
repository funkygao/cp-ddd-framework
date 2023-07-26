/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.dsl;

import java.lang.annotation.*;

/**
 * 关键领域事件，单向传递.
 *
 * <p>Example:</p>
 * <pre>
 * {@code
 *
 * ℗KeyEvent(type = KeyEvent.Type.Local)
 * class OrderShipped {}
 * }
 * </pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface KeyEvent {

    /**
     * 补充说明.
     */
    String remark() default "";
}
