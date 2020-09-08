package org.ddd.cp.ddd.ext;

import org.ddd.cp.ddd.model.IDomainModel;

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
