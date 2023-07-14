/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ext;

import io.github.dddplus.model.IDomainService;
import lombok.NonNull;

/**
 * 业务身份解析器(业务特征识别).
 */
public interface IIdentityResolver<Identity extends IIdentity> extends IPlugable, IDomainService {

    /**
     * 根据业务身份判断是否属于我的业务：准入规则(条件).
     *
     * @param identity 业务身份，业务特征
     * @return true if yes
     */
    boolean match(@NonNull Identity identity);
}
