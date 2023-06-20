/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model;

import lombok.NonNull;

/**
 * 引用对象.
 *
 * @param <T> 被引用对象类型.
 */
public class Ref<T> extends AbstractBusinessNo<T> {
    public Ref(@NonNull T value) {
        super(value);
    }
}
