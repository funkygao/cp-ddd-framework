/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.cdf.ddd.step;

import org.cdf.ddd.model.IDomainModel;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 修订后续步骤的异常.
 * <p>
 * <p>配合{@link IDomainStep#execute(IDomainModel)}的异常使用，在某一步骤抛出该异常来修订后续步骤</p>
 * <p>可能产生的死循环(a -> b(revise) -> a)，由使用者负责，暂时不提供dead loop检测：因为即使检测到也不知道如何处理，它本身就是bug</p>
 * <p>IMPORTANT: 不要在领域层异常直接实现该接口，应该创建新的异常类，否则会与步骤的回滚机制冲突！</p>
 */
public interface IDecideStepsException {

    /**
     * 修订后的后续步骤编号.
     *
     * @return subsequent step code list
     */
    @NotNull
    List<String> subsequentSteps();
}
