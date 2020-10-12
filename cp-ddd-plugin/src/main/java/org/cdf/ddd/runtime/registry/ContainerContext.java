/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.cdf.ddd.runtime.registry;

import org.cdf.ddd.plugin.IContainerContext;
import org.springframework.context.ApplicationContext;

import javax.validation.constraints.NotNull;

/**
 * 默认的容器上下文实现.
 * <p>
 * <p>有些{@code Plugin}不希望Spring加载，但需要中台的资源(例如：RPC/Redis/MQ)，中台输出受限的Spring容器供使用</p>
 */
final class ContainerContext implements IContainerContext {
    private final ApplicationContext applicationContext;

    ContainerContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public <T> T getBean(@NotNull Class<T> requiredType) throws RuntimeException {
        return applicationContext.getBean(requiredType);
    }

    @Override
    public <T> T getBean(@NotNull String name, @NotNull Class<T> requiredType) throws RuntimeException {
        return applicationContext.getBean(name, requiredType);
    }
}
