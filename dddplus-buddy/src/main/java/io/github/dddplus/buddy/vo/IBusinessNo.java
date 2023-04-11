package io.github.dddplus.buddy.vo;

import io.github.dddplus.model.IIdentity;

import java.io.Serializable;

/**
 * 业务编号的统一标准.
 *
 * @param <T> 业务编号的数据类型
 */
public interface IBusinessNo<T> extends Serializable, IIdentity {

    T value();
}
