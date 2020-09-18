/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.cdf.ddd.plugin;

import java.lang.annotation.Annotation;

/**
 * 容器提供给插件的上下文信息.
 */
public interface IContainerContext {

    /**
     * 注册.
     *
     * @param annotation 注解类型，目前只支持{@code Project, Pattern, Extension}
     * @param object     对象实例
     * @throws Exception
     */
    void registerBean(Class<? extends Annotation> annotation, Object object) throws Exception;

    /**
     * 注销.
     *
     * @param annotation 注解类型，目前只支持{@code Project, Pattern, Extension}
     * @param object     对象实例
     * @throws Exception
     */
    void deregisterBean(Class<? extends Annotation> annotation, Object object) throws Exception;

}
