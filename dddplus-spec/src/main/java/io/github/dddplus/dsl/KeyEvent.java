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
 * ℗KeyEvent(type = KeyEvent.Type.Local)
 * class OrderShipped {}
 * }
 * </pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface KeyEvent {
    /**
     * 事件类型.
     */
    Type type();

    /**
     * 补充说明.
     */
    String remark() default "";

    enum Type {
        /**
         * 基于内存本地事件.
         */
        Local,

        /**
         * 基于MQ Broker的消息消费.
         */
        RemoteConsuming,

        /**
         * 基于MQ Broker的消息生产.
         */
        RemoteProducing,
    }
}
