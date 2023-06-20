package io.github.dddplus.model;

import lombok.NonNull;

/**
 * 业务对象的引用.
 *
 * @param <T> 引用的数据类型.
 */
public class Ref<T> extends AbstractBusinessNo<T> {
    public Ref(@NonNull T value) {
        super(value);
    }
}
