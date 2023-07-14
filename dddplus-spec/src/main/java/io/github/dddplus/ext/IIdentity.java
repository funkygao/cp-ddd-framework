/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ext;

/**
 * 扩展点的业务身份.
 *
 * <p>从中可以获取业务特征，以便路由扩展点实例.</p>
 * <p>业务身份，本质是产生业务变化的来源，技术上表现为维度(字段的组合).</p>
 * <ul>使用注意：
 * <li>{@link IIdentity}不意味着每次路由扩展点时你都要定义新的业务身份类，很可能让现有的领域模型类实现本接口即可</li>
 * <li>业务身份属于领域层概念，{@code DTO}不能是业务身份</li>
 * </ul>
 *
 * @see io.github.dddplus.ext.IIdentityResolver#match(IIdentity)
 * @see io.github.dddplus.ext.IPolicy#extensionCode(IIdentity)
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
