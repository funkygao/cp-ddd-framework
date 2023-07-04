/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model;

import java.util.HashMap;
import java.util.Map;

/**
 * An Exchange is the container holding the transient data.
 * <p/>
 * <p>{@link Exchange} is part of our DDD building block.</p>
 * <p>Named after {@code Apache Camel Exchange}.</p>
 * <p>IMPORTANT: {@link Exchange}的状态是{@code transient}的，不能跨进程传递.</p>
 * <p>由于legacy code与DDD code会并存很长时间，为了方便legacy code改动又不导致DDD code腐化，引入{@link Exchange}.</p>
 * <p>一个{@code Entity/ValueObject}有时候需要携带一些数据用于不同方法间交换使用，一处写，它处读，而这些属性不属于模型本身固有属性，就可以把这些程序员属性收敛到{@link Exchange}内.</p>
 * <p>例子，订单取消场景：</p>
 * <p>legacy code根据取消的单据号取出相应的复核任务列表，一个方法内遍历复核任务列表计算该任务是否该返架，另外一个方法遍历该列表，根据是否返架修改任务状态.</p>
 * <p>如果把{@code boolean needRestow}加到{@code CheckTask}上，就腐化了{@code CheckTask}，这时候就可以：</p>
 * <pre>
 * {@code
 *
 * class CheckTask {
 *     private final transient Exchange exchange = new Exchange();
 *
 *     public Exchange exchange() {
 *         return exchange;
 *     }
 * }
 *
 * class LegacyCode {
 *     void foo() {
 *         checkTask.exchange().set("needRestow", true);
 *     }
 *
 *     void bar() {
 *         if (checkTask.exchange().is("needRestow")) {
 *             // update check task status
 *         }
 *     }
 * }
 * }
 * </pre>
 */
public final class Exchange {
    private final transient Map<String, Object> holder = new HashMap<>();

    /**
     * 某个key是否存在.
     *
     * @param key
     * @return
     */
    public boolean exists(String key) {
        return holder.get(key) != null;
    }

    /**
     * 为某个key赋值.
     *
     * <p>如果该key上已经有值了，直接覆盖</p>
     * <p>允许同一个key，被赋值成不同类型的值：使用者为此负责</p>
     *
     * @param key
     * @param value
     */
    public void set(String key, Object value) {
        holder.put(key, value);
    }

    /**
     * 获取key对应的值.
     *
     * @param key
     * @param valueType
     * @param <T>
     * @return
     * @throws ClassCastException 如果搞错了对应值的类型
     */
    public <T> T get(String key, Class<T> valueType) throws ClassCastException {
        Object value = holder.get(key);
        if (value == null) {
            return null;
        }

        return valueType.cast(value);
    }

    /**
     * 方便的布尔类型值取值.
     *
     * @param key
     * @return
     * @throws ClassCastException 如果搞错了对应值的类型
     */
    public boolean is(String key) throws ClassCastException {
        Boolean b = get(key, Boolean.class);
        if (b == null) {
            return false;
        }

        return b;
    }

    public void clear() {
        holder.clear();
    }

    // for testing only
    int size() {
        return holder.size();
    }

}

