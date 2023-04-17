/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model;

import io.github.dddplus.ext.IIdentityResolver;

/**
 * 业务身份.
 * <p>
 * <p>从中可以获取业务特征，以便路由扩展点</p>.
 *
 * @see io.github.dddplus.ext.IIdentityResolver
 */
public interface IIdentity {

    /**
     * 是否匹配指定的业务身份.
     *
     * @param resolvers 业务身份解析器
     * @return
     */
    default boolean matchAny(IIdentityResolver... resolvers) {
        for (IIdentityResolver resolver : resolvers) {
            if (resolver.match(this)) {
                return true;
            }
        }

        return false;
    }
}
