/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.runtime.registry;

import io.github.dddplus.runtime.interceptor.IExtensionInterceptor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@ToString
class InterceptorDef implements IRegistryAware {

    @Getter
    private IExtensionInterceptor interceptorBean;

    @Override
    public void registerBean(@NonNull Object bean) {
        this.interceptorBean = (IExtensionInterceptor) InternalAopUtils.getTarget(bean);
        InternalIndexer.index(this);
    }
}
