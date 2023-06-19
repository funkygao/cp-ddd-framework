/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.step;

import io.github.dddplus.model.IDomainModel;
import lombok.NonNull;

/**
 * 可以回滚的活动步骤.
 * <p>
 * <p>Sagas模式</p>
 */
@Deprecated
public interface IRevokableDomainStep<Model extends IDomainModel, Ex extends RuntimeException> extends IDomainStep<Model, Ex> {

    /**
     * 执行本步骤的回滚操作，进行冲正.
     * <p>
     * <p>Best effort就好，Sagas模式并不能严格保证一致性</p>
     *
     * @param model 领域模型
     * @param cause {@link IDomainStep#execute(IDomainModel)}执行过程中抛出的异常，即回滚原因
     */
    void rollback(@NonNull Model model, @NonNull Ex cause);
}
