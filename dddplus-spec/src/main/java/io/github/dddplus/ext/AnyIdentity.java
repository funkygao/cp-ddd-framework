/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ext;

import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * 万能业务身份，可以承载任意形式的业务身份.
 *
 * <p>有时候多个类共同承载业务身份/特征，而为此定义一个业务身份类来容纳这些类时，可能很难从模型上定义这个类是什么.</p>
 * <p>例如，订单和任务在一起才能用于判断业务身份</p>
 * <pre>
 * {@code
 *
 * class MyIdentity implements IIdentity {
 *     final Task task;
 *     final Order order;
 *     public MyIdentity(Task task, Order order) {}
 * }
 * }
 * </pre>
 * <p>为了解决这个问题，我们提供了通用万能的{@link AnyIdentity}，不必为此边缘场景额外定义新的业务身份类：</p>
 * <pre>
 * {@code
 *
 * IIdentity identity = AnyIdentity.newIdentity();
 * identity.put("task", task).put("order", order);
 * Task task = identity.get("task", Task.class);
 * }
 * </pre>
 * <p>{@link AnyIdentity}牺牲了业务语义，换来了通用性.</p>
 */
public final class AnyIdentity implements IIdentity {
    private final Map<String, Object> data = new HashMap<>();

    private AnyIdentity() {
    }

    /**
     * 创建一个新的业务身份实例.
     */
    public static AnyIdentity newIdentity() {
        return new AnyIdentity();
    }

    /**
     * 根据key暂存值.
     *
     * <p>由于{@link AnyIdentity}仅用于扩展点路由的手递手信息传递，作用域小，因此{@code key}的命名不必特别严格，也不必解决{@code magic number}问题.</p>
     */
    public AnyIdentity put(@NonNull String key, Object value) {
        data.put(key, value);
        return this;
    }

    /**
     * 指定value类型获取对应key的值。
     *
     * @param key       key name
     * @param valueType value type
     * @param <T>
     * @return value of the specified key; null if key not present
     * @throws ClassCastException 如果指定的value类型错了
     */
    public <T> T get(@NonNull String key, @NonNull Class<T> valueType) throws ClassCastException {
        Object value = data.get(key);
        if (value == null) {
            return null;
        }

        return valueType.cast(value);
    }

}
