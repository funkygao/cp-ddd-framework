/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model;

import java.io.Serializable;

/**
 * Persistent Object.
 *
 * <p>Po resides in {@code infrastructure layer}，被认为是技术细节：行为是抽象，数据是细节，行为封装数据.</p>
 * <p>这里建议使用{@code mapstruct}把{@link IPo}对象转换为{@link IDomainModel}.</p>
 */
public interface IPo extends Serializable {
}
