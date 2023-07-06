/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model;

/**
 * 统一定义的业务编号类.
 *
 * <p>已经封装数据的存取.</p>
 * <p>IMPORTANT: value不能为null</p>
 *
 * @param <T> 业务编号的数据类型
 */
public abstract class AbstractBusinessNo<T> implements IBusinessNo<T> {
    protected final T value;

    protected AbstractBusinessNo(T value) {
        this.value = value;
    }

    /**
     * 值是否存在，即是否非空.
     */
    public boolean isPresent() {
        return this.value != null;
    }

    @Override
    public final T value() {
        return value;
    }

    @Override
    public boolean equals(Object that) {
        if (that != null && that.getClass() == this.getClass()) {
            // 相同的 AbstractBusinessNo 类型，则比较其 value
            // 不考虑继承场景：A extends B，那么可以 a.equals(b) 吗？No!
            IBusinessNo thatNo = (IBusinessNo) that;
            return this.value.equals(thatNo.value());
        }

        if (value.getClass().isInstance(that)) {
            // 值的类型与比较的对象类型相同，也可以比较
            return this.value.equals(that);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
