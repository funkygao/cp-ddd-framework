/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model;

import io.github.dddplus.ext.IIdentityResolver;

/**
 * 业务身份.
 *
 * <p>从中可以获取业务特征，以便路由扩展点.</p>
 *
 * @see io.github.dddplus.ext.IIdentityResolver
 */
public interface IIdentity {

    /**
     * 本业务身份是否满足指定业务身份解析器里任意一个.
     *
     * <p>即，本业务特征是否属于某业务场景.</p>
     *
     * @param resolvers 业务身份解析器
     * @return true if yes
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
