/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model;

import lombok.Getter;
import lombok.NonNull;

/**
 * 统一定义的业务编号类.
 *
 * <p>已经封装数据的存取.</p>
 * <p>IMPORTANT: value不能为null</p>
 *
 * @param <T> 业务编号的数据类型
 */
public abstract class AbstractBusinessNo<T> implements IBusinessNo<T> {
    @Getter
    protected final T value;

    protected AbstractBusinessNo(@NonNull T value) {
        this.value = value;
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
