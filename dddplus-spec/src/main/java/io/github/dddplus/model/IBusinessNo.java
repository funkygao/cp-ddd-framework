/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model;

import io.github.dddplus.ext.IIdentity;

import java.io.Serializable;

/**
 * 业务编号的统一标准.
 *
 * @param <T> 业务编号的数据类型
 */
public interface IBusinessNo<T> extends Serializable, IIdentity, IValueObject {

    T value();
}
