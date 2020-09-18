/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.cdf.ddd.runtime.registry;

import lombok.extern.slf4j.Slf4j;
import org.cdf.ddd.annotation.Partner;
import org.cdf.ddd.annotation.Pattern;
import org.cdf.ddd.plugin.IPluginListener;

import javax.validation.constraints.NotNull;

/**
 * 业务容器.
 */
@Slf4j
public class Container {
    private static final Container instance = new Container();

    private Container() {
    }

    /**
     * 获取业务容器单例.
     */
    @NotNull
    public static Container getInstance() {
        return instance;
    }

    /**
     * 加载业务前台jar包.
     *
     * @param jarPath     jar path
     * @param basePackage Spring component-scan base-package值，但不支持逗号分隔. if null, will not scan Spring
     * @throws Exception
     */
    public void loadPartner(@NotNull String jarPath, String basePackage) throws Exception {
        if (!jarPath.endsWith(".jar")) {
            throw new IllegalArgumentException("Invalid jarPath: " + jarPath);
        }

        long t0 = System.nanoTime();
        log.warn("loading partner:{} basePackage:{}", jarPath, basePackage);

        try {
            new PartnerLoader().load(jarPath, basePackage);
        } catch (Exception ex) {
            log.error("load partner:{}, cost {}ms", jarPath, (System.nanoTime() - t0) / 1000_000, ex);

            throw ex;
        }

        // bingo!
        getListener().onLoad(new ContainerContext());

        log.warn("loaded partner:{}, cost {}ms", jarPath, (System.nanoTime() - t0) / 1000_000);
    }

    /**
     * 注销一个业务前台身份.
     *
     * @param code {@link Partner#code()}
     */
    public void unloadPartner(@NotNull String code) {
        InternalIndexer.removePartner(code);
    }

    /**
     * 加载业务模式jar包.
     *
     * @param jarPath     jar path
     * @param basePackage Spring component-scan base-package值，但不支持逗号分隔. if null, will not scan Spring
     * @throws Exception
     */
    public void loadPattern(@NotNull String jarPath, String basePackage) throws Exception {

    }

    /**
     * 卸载业务模式.
     *
     * @param code {@link Pattern#code()}
     */
    public void unloadPattern(@NotNull String code) {
        InternalIndexer.removePattern(code);
    }

    private IPluginListener getListener() {
        // TODO 多个业务jar，会报错
        return DDDBootstrap.applicationContext().getBean(IPluginListener.class);
    }
}
