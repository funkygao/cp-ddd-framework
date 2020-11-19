/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.runtime;

import io.github.dddplus.ext.IDecideStepsExt;
import io.github.dddplus.model.IDomainModel;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

/**
 * 基础决策领域步骤的能力.
 *
 * @param <Model> 领域模型
 */
public abstract class BaseDecideStepsAbility<Model extends IDomainModel> extends BaseDomainAbility<Model, IDecideStepsExt> {
    private static final IDecideStepsExt defaultExt = new EmptyExt();

    /**
     * 根据领域模型和领域活动码决定需要执行哪些领域步骤.
     *
     * @param model        领域模型
     * @param activityCode 领域活动码
     * @return step code list
     */
    public List<String> decideSteps(@NotNull Model model, String activityCode) {
        return firstExtension(model).decideSteps(model, activityCode);
    }

    @Override
    public IDecideStepsExt defaultExtension(@NotNull Model model) {
        return defaultExt;
    }

    private static class EmptyExt implements IDecideStepsExt {
        private static List<String> emptySteps = Collections.emptyList();

        @Override
        public List<String> decideSteps(@NotNull IDomainModel model, @NotNull String activityCode) {
            return emptySteps;
        }
    }
}
