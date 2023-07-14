/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.runtime;

import io.github.dddplus.ext.IDecideStepsExt;
import io.github.dddplus.ext.IIdentity;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;

/**
 * 基础决策领域步骤的路由器.
 *
 * @param <Identity> 业务身份
 */
@Deprecated
public abstract class BaseDecideStepsRouter<Identity extends IIdentity> extends BaseRouter<IDecideStepsExt, Identity> {
    private static final IDecideStepsExt defaultExt = new EmptyExt();

    /**
     * 根据领域模型和领域活动码决定需要执行哪些领域步骤.
     *
     * @param identity     业务身份
     * @param activityCode 领域活动码
     * @return step code list
     */
    public List<String> decideSteps(@NonNull Identity identity, String activityCode) {
        return firstExtension(identity).decideSteps(identity, activityCode);
    }

    @Override
    public IDecideStepsExt defaultExtension(@NonNull Identity identity) {
        return defaultExt;
    }

    private static class EmptyExt implements IDecideStepsExt {
        private static List<String> emptySteps = Collections.emptyList();

        @Override
        public List<String> decideSteps(@NonNull IIdentity identity, @NonNull String activityCode) {
            return emptySteps;
        }
    }
}
