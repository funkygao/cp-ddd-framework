/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.plugin;

/**
 * 插件的启动、关闭监听器，是插件包加载卸载的入口.
 */
public interface IPluginListener {

    /**
     * 插件包prepare完成时触发.
     * <p>
     * <p>此时，插件包里的类刚刚加载和实例化，还没有被调用</p>
     *
     * @param ctx 容器上下文
     * @throws Exception
     */
    void onPrepared(IContainerContext ctx) throws Exception;

    /**
     * 插件包切换完成时触发.
     * <p>
     * <p>此时，相应的请求会发送该插件包里的类</p>
     *
     * @param ctx 容器上下文
     * @throws Exception
     */
    void onCommitted(IContainerContext ctx) throws Exception;
}
