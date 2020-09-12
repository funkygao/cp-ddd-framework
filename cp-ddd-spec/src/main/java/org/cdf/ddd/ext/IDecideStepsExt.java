/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.cdf.ddd.ext;

import org.cdf.ddd.model.IDomainModel;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用于编排领域步骤的扩展点.
 */
public interface IDecideStepsExt extends IDomainExtension {

    /**
     * 根据领域模型和领域活动码决定需要执行哪些领域步骤.
     *
     * @param model        领域模型
     * @param activityCode 领域活动码
     * @return step code list
     */
    @NotNull
    List<String> decideSteps(@NotNull IDomainModel model, @NotNull String activityCode);
}
