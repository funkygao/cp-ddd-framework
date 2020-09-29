/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.cdf.ddd.runtime.registry;

import lombok.extern.slf4j.Slf4j;
import org.cdf.ddd.annotation.Partner;
import org.cdf.ddd.annotation.Pattern;
import org.cdf.ddd.annotation.UnderDevelopment;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 业务容器，用于动态加载个性化业务包：Plugin Jar.
 * <p>
 * <p>Plugin Jar是可以被动态加载的Jar = (Pattern + Extension) | (Partner + Extension)</p>
 * <p>{@code Container}常驻内存，{@code PluginJar}动态加载：动静分离</p>
 * <p>
 * <pre>
 *                                                  +- 1 JDKClassLoader
 * Container -> Plugin -> PluginClassLoader --------|- 1 ContainerClassLoader
 *                              | loadClass         +- N PluginClassLoader
 *                        +---------------------+
 *                        |                     |
 *                  (Partner | Pattern)      Extension
 * </pre>
 */
@Slf4j
@UnderDevelopment
public final class Container {
    private static final Container instance = new Container();

    private static ClassLoader jdkClassLoader = initJDKClassLoader();
    private static ClassLoader containerClassLoader = Container.class.getClassLoader();

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
     * @param jarUrl      Plugin jar URL
     * @param basePackage Spring component-scan base-package值，但不支持逗号分隔. if null, will not scan Spring
     * @throws Throwable
     */
    public void loadPartnerPlugin(@NotNull URL jarUrl, String basePackage) throws Throwable {
        File localJar = jarTempLocalFile(jarUrl);
        localJar.deleteOnExit();
        log.info("loadPartnerPlugin {} -> {}", jarUrl, localJar.getCanonicalPath());
        FileUtils.copyInputStreamToFile(jarUrl.openStream(), localJar);
        loadPartnerPlugin(localJar.getAbsolutePath(), basePackage);
    }

    /**
     * 加载业务前台jar包.
     * <p>
     * <p>如果使用本动态加载，就不要maven里静态引入业务前台jar包依赖了.</p>
     *
     * @param jarPath     jar path
     * @param basePackage Spring component-scan base-package值，但不支持逗号分隔. if null, will not scan Spring
     * @throws Throwable
     */
    public void loadPartnerPlugin(@NotNull String jarPath, String basePackage) throws Throwable {
        if (!jarPath.endsWith(".jar")) {
            throw new IllegalArgumentException("Invalid jarPath: " + jarPath);
        }

        long t0 = System.nanoTime();
        log.warn("loading partner:{} basePackage:{}", jarPath, basePackage);
        try {
            new Plugin(jdkClassLoader, containerClassLoader).
                    load(jarPath, basePackage, Partner.class, new ContainerContext());
        } catch (Throwable ex) {
            log.error("fails to load partner:{}, cost {}ms", jarPath, (System.nanoTime() - t0) / 1000_000, ex);

            throw ex;
        }

        log.warn("loaded partner:{}, cost {}ms", jarPath, (System.nanoTime() - t0) / 1000_000);
    }

    /**
     * 注销一个业务前台身份.
     *
     * @param code {@link Partner#code()}
     */
    public void unloadPartnerPlugin(@NotNull String code) {
        log.warn("unloading partner:{}", code);
        InternalIndexer.removePartner(code);
        log.warn("unloaded partner:{}", code);
    }

    /**
     * 加载业务模式jar包.
     *
     * @param jarUrl      Plugin jar URL
     * @param basePackage Spring component-scan base-package值，但不支持逗号分隔. if null, will not scan Spring
     * @throws Throwable
     */
    public void loadPatternPlugin(@NotNull URL jarUrl, String basePackage) throws Throwable {
        File localJar = jarTempLocalFile(jarUrl);
        localJar.deleteOnExit();

        log.info("loadPatternPlugin {} -> {}", jarUrl, localJar.getCanonicalPath());
        FileUtils.copyInputStreamToFile(jarUrl.openStream(), localJar);
        loadPatternPlugin(localJar.getAbsolutePath(), basePackage);
    }

    /**
     * 加载业务模式jar包.
     *
     * @param jarPath     jar path
     * @param basePackage Spring component-scan base-package值，但不支持逗号分隔. if null, will not scan Spring
     * @throws Throwable
     */
    public void loadPatternPlugin(@NotNull String jarPath, String basePackage) throws Throwable {
        if (!jarPath.endsWith(".jar")) {
            throw new IllegalArgumentException("Invalid jarPath: " + jarPath);
        }

        long t0 = System.nanoTime();
        log.warn("loading pattern:{} basePackage:{}", jarPath, basePackage);
        try {
            new Plugin(jdkClassLoader, containerClassLoader).
                    load(jarPath, basePackage, Pattern.class, new ContainerContext());
        } catch (Throwable ex) {
            log.error("fails to load pattern:{}, cost {}ms", jarPath, (System.nanoTime() - t0) / 1000_000, ex);

            throw ex;
        }

        log.warn("loaded pattern:{}, cost {}ms", jarPath, (System.nanoTime() - t0) / 1000_000);
    }

    /**
     * 卸载业务模式.
     *
     * @param code {@link Pattern#code()}
     */
    public void unloadPatternPlugin(@NotNull String code) {
        log.warn("unloading pattern:{}", code);
        InternalIndexer.removePattern(code);
        log.warn("unloaded pattern:{}", code);
    }

    File jarTempLocalFile(@NotNull URL jarUrl) throws IOException {
        String prefix = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String suffix = jarUrl.getPath().substring(jarUrl.getPath().lastIndexOf("/") + 1);
        return File.createTempFile(prefix, "." + suffix);
    }

    private static ClassLoader initJDKClassLoader() {
        ClassLoader parent;
        for (parent = ClassLoader.getSystemClassLoader(); parent.getParent() != null; parent = parent.getParent()) {
        }

        List<URL> jdkUrls = new ArrayList<>(100);
        try {
            // javaHome: /Library/Java/JavaVirtualMachines/jdk1.8.0_40.jdk/Contents/Home
            String javaHome = System.getProperty("java.home").replace(File.separator + "jre", "");
            // search path of URLs for loading classes and resources
            URL[] urls = ((URLClassLoader) ClassLoader.getSystemClassLoader()).getURLs();
            for (URL url : urls) {
                if (url.getPath().startsWith(javaHome)) {
                    // 只找JDK本身的
                    jdkUrls.add(url);
                }
            }
        } catch (Throwable shouldNeverHappen) {
            log.error("JDKClassLoader", shouldNeverHappen);
        }

        return new URLClassLoader(jdkUrls.toArray(new URL[0]), parent);
    }

    ClassLoader getJdkClassLoader() {
        return jdkClassLoader;
    }

    ClassLoader getContainerClassLoader() {
        return containerClassLoader;
    }
}
