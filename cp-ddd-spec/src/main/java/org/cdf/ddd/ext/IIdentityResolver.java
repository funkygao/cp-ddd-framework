/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.cdf.ddd.ext;

import org.cdf.ddd.model.IDomainModel;

import javax.validation.constraints.NotNull;

/**
 * 业务身份解析器.
 */
public interface IIdentityResolver<Model extends IDomainModel> {

    /**
     * 根据领域模型判断是否属于我的业务.
     *
     * @param model 领域模型
     * @return true if yes
     */
    boolean match(@NotNull Model model);
}
