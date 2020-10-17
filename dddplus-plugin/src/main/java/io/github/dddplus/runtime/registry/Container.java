/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.runtime.registry;

import io.github.dddplus.annotation.Partner;
import io.github.dddplus.annotation.UnderDevelopment;
import io.github.dddplus.plugin.IPlugin;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 业务容器，用于动态加载个性化业务包：Plugin Jar.
 * <p>
 * <p>{@code Container}常驻内存，{@code PluginJar}动态加载：动静分离</p>
 * <p>
 * <pre>
 *    +- 1 ContainerClassLoader
 *    |- 1 JDKClassLoader
 *    |                     +- pluginApplicationContext
 *    |                     |
 * Container ----> Plugin --+- pluginClassLoader
 *             N                    | loadClass
 *                        +---------------------+
 *                        |                     |
 *                  (Partner | Pattern)      Extension
 * </pre>
 */
@Slf4j
@UnderDevelopment // TODO Plugin Jar动态加载机制可以工作，但尚未经过严格的测试，请不要生产环境使用!
public final class Container {
    private static final Container instance = new Container();

    private static ClassLoader jdkClassLoader = initJDKClassLoader();
    private static ClassLoader containerClassLoader = Container.class.getClassLoader();

    private static final Map<String, IPlugin> activePlugins = new HashMap<>(); // has no concurrent scenarios: thread safe

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
     * 获取当前所有活跃的{@code Plugin}.
     *
     * @return key: Plugin code
     */
    @NotNull
    public Map<String, IPlugin> getActivePlugins() {
        return activePlugins;
    }

    /**
     * 加载业务前台jar包.
     *
     * @param code      {@link IPlugin#getCode()}
     * @param version   version of the jar
     * @param jarUrl    Plugin jar URL
     * @param useSpring jar包里是否需要Spring机制
     * @throws Throwable
     */
    public synchronized void loadPartnerPlugin(@NotNull String code, @NotNull String version, @NotNull URL jarUrl, boolean useSpring) throws Throwable {
        File localJar = createLocalFile(jarUrl);
        localJar.deleteOnExit();

        log.info("loadPartnerPlugin {} -> {}", jarUrl, localJar.getCanonicalPath());
        FileUtils.copyInputStreamToFile(jarUrl.openStream(), localJar);
        loadPartnerPlugin(code, version, localJar.getAbsolutePath(), useSpring);
    }

    /**
     * 加载业务前台jar包.
     * <p>
     * <p>如果使用本动态加载，就不要maven里静态引入业务前台jar包依赖了.</p>
     *
     * @param code      {@link IPlugin#getCode()}
     * @param version   version of the jar
     * @param jarPath   jar path
     * @param useSpring jar包里是否需要Spring机制
     * @throws Throwable
     */
    public synchronized void loadPartnerPlugin(@NotNull String code, @NotNull String version, @NotNull String jarPath, boolean useSpring) throws Throwable {
        if (!jarPath.endsWith(".jar")) {
            throw new IllegalArgumentException("Invalid jarPath: " + jarPath);
        }

        long t0 = System.nanoTime();
        log.warn("Loading partner:{} useSpring:{}", jarPath, useSpring);
        try {
            Plugin plugin = new Plugin(code, version, jdkClassLoader, containerClassLoader);
            plugin.load(jarPath, useSpring, Partner.class, new ContainerContext(DDDBootstrap.applicationContext()));

            Plugin pluginToDestroy = (Plugin) activePlugins.get(code);
            if (pluginToDestroy != null) {
                log.warn("to destroy partner:{} ver:{}", code, plugin.getVersion());
                pluginToDestroy.onDestroy();
            }

            activePlugins.put(plugin.getCode(), plugin); // old plugin will be GC'ed eventually

            log.warn("Loaded partner:{}, cost {}ms", jarPath, (System.nanoTime() - t0) / 1000_000);
        } catch (Throwable ex) {
            log.error("fails to load partner:{}, cost {}ms", jarPath, (System.nanoTime() - t0) / 1000_000, ex);

            throw ex;
        }
    }

    File createLocalFile(@NotNull URL jarUrl) throws IOException {
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
}
