/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.cdf.ddd.runtime.registry;

import lombok.extern.slf4j.Slf4j;
import org.cdf.ddd.annotation.Partner;
import org.cdf.ddd.annotation.Pattern;

import javax.validation.constraints.NotNull;

/**
 * 业务容器，用于动态加载个性化业务包：Plugin.
 * <p>
 * <pre>
 *                                                          +- JDKClassLoader 1
 * Container -> JarDynamicLoader -> CustomBizClassLoader ---|- ContainerClassLoader 1
 *                                          | loadClass     +- CustomBizClassLoader N
 *                                +---------------------+
 *                                |                     |
 *                          (Partner | Pattern)      Extension
 * </pre>
 */
@Slf4j
public class Container {
    private static final Container instance = new Container();
    private static final JarDynamicLoader jarDynamicLoader = new JarDynamicLoader();

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
     * <p>
     * <p>如果使用本动态加载，就不要maven里静态引入业务前台jar包依赖了.</p>
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
            jarDynamicLoader.load(jarPath, basePackage, Partner.class, new ContainerContext());
        } catch (Exception ex) {
            log.error("load partner:{}, cost {}ms", jarPath, (System.nanoTime() - t0) / 1000_000, ex);

            throw ex;
        }

        log.warn("loaded partner:{}, cost {}ms", jarPath, (System.nanoTime() - t0) / 1000_000);
    }

    /**
     * 注销一个业务前台身份.
     *
     * @param code {@link Partner#code()}
     */
    public void unloadPartner(@NotNull String code) {
        log.warn("unloading partner:{}", code);
        InternalIndexer.removePartner(code);
        log.warn("unloaded partner:{}", code);
    }

    /**
     * 加载业务模式jar包.
     *
     * @param jarPath     jar path
     * @param basePackage Spring component-scan base-package值，但不支持逗号分隔. if null, will not scan Spring
     * @throws Exception
     */
    public void loadPattern(@NotNull String jarPath, String basePackage) throws Exception {
        if (!jarPath.endsWith(".jar")) {
            throw new IllegalArgumentException("Invalid jarPath: " + jarPath);
        }

        long t0 = System.nanoTime();
        log.warn("loading pattern:{} basePackage:{}", jarPath, basePackage);
        try {
            jarDynamicLoader.load(jarPath, basePackage, Pattern.class, new ContainerContext());
        } catch (Exception ex) {
            log.error("load pattern:{}, cost {}ms", jarPath, (System.nanoTime() - t0) / 1000_000, ex);

            throw ex;
        }

        log.warn("loaded pattern:{}, cost {}ms", jarPath, (System.nanoTime() - t0) / 1000_000);
    }

    /**
     * 卸载业务模式.
     *
     * @param code {@link Pattern#code()}
     */
    public void unloadPattern(@NotNull String code) {
        log.warn("unloading pattern:{}", code);
        InternalIndexer.removePattern(code);
        log.warn("unloaded pattern:{}", code);
    }

}
