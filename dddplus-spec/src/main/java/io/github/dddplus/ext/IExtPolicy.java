/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ext;

import io.github.dddplus.model.IDomainModel;

import javax.validation.constraints.NotNull;

/**
 * 扩展点定位策略.
 *
 * <p>不同于 {@link IIdentityResolver} 的静态绑定，扩展点定位策略是动态的.</p>
 * <p>每一个扩展点定位策略只能有一个实例，并且绑定到一个扩展点.</p>
 */
public interface IExtPolicy<Model extends IDomainModel> {

    /**
     * 根据领域模型，定位匹配的扩展点.
     *
     * @param model 领域模型
     * @return 匹配的扩展点编码, SHOULD NEVER be null
     */
    @NotNull
    String extensionCode(@NotNull Model model);
}
