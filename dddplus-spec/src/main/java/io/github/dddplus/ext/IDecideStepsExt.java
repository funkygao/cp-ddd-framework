/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ext;

import lombok.NonNull;

import java.util.List;

/**
 * 用于编排领域步骤的扩展点.
 */
@Deprecated
public interface IDecideStepsExt extends IDomainExtension {

    /**
     * 根据领域模型和领域活动码决定需要执行哪些领域步骤.
     *
     * @param identity     业务身份
     * @param activityCode 领域活动码
     * @return step code list
     */
    @NonNull
    List<String> decideSteps(@NonNull IIdentity identity, @NonNull String activityCode);
}
