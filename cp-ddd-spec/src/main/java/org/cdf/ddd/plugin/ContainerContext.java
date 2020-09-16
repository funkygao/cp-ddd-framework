/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.cdf.ddd.plugin;

import org.cdf.ddd.ext.IDomainExtension;

/**
 * 容器提供给插件的上下文信息.
 */
public interface ContainerContext {

    /**
     * 注册扩展点实例.
     *
     * @param extClazz 扩展点类型
     * @param ext      扩展点实例
     */
    void registerExtension(Class<? extends IDomainExtension> extClazz, Object ext);

    /**
     * 注销扩展点实例.
     *
     * @param extClazz 扩展点类型
     * @param ext      扩展点实例
     */
    void deregisterExtension(Class<? extends IDomainExtension> extClazz, Object ext);

    /**
     * 注册业务前台身份.
     *
     * @param partner 业务前台身份
     */
    void registerPartner(Object partner);

    /**
     * 注销业务前台身份.
     *
     * @param partner 业务前台身份
     */
    void deregisterPartner(Object partner);

}
