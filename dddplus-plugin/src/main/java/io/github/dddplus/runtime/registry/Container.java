/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.runtime.registry;

import io.github.dddplus.annotation.Partner;
import io.github.dddplus.plugin.IContainerContext;
import io.github.dddplus.plugin.IPlugin;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

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
 *    +- 1 containerClassLoader
 *    |- 1 jdkClassLoader
 *    |- 1 containerApplicationContext
 *    |
 *    |                     +- pluginApplicationContext
 *    |                     |
 * Container ----> Plugin --+- pluginClassLoader
 *             N                    | loadClass
 *                        +---------------------+
 *                        |                     |
 *                  [Partner | Pattern]      Extension
 * </pre>
 */
@Slf4j
public final class Container {
    private static final Container instance = new Container();

    private static final ClassLoader jdkClassLoader = initJDKClassLoader();
    private static final ClassLoader containerClassLoader = Container.class.getClassLoader();
    private static final ApplicationContext containerApplicationContext = DDDBootstrap.applicationContext();

    private static final Map<String, IPlugin> activePlugins = new HashMap<>(); // has no concurrent scenarios: thread safe

    private Container() {
    }

    /**
     * 获取业务容器单例.
     */
    @NonNull
    public static Container getInstance() {
        return instance;
    }

    /**
     * 获取当前所有活跃的{@code Plugin}.
     *
     * @return key: Plugin code
     */
    @NonNull
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
    public synchronized void loadPartnerPlugin(@NonNull String code, @NonNull String version, @NonNull URL jarUrl, boolean useSpring) throws Throwable {
        File localJar = createLocalFile(jarUrl);
        localJar.deleteOnExit();

        log.info("loadPartnerPlugin {} -> {}", jarUrl, localJar.getCanonicalPath());
        FileUtils.copyInputStreamToFile(jarUrl.openStream(), localJar);
        loadPartnerPlugin(code, version, localJar.getAbsolutePath(), useSpring);
    }

    /**
     * 加载业务前台jar包，支持定制IContainerContext的实现.
     * <p>
     * <p>如果使用本动态加载，就不要maven里静态引入业务前台jar包依赖了.</p>
     *
     * @param code      {@link IPlugin#getCode()}
     * @param version   version of the jar
     * @param jarPath   jar path
     * @param useSpring jar包里是否需要Spring机制
     * @param containerContext container context instance
     * @throws Throwable
     */
    public synchronized void loadPartnerPlugin(@NonNull String code, @NonNull String version, @NonNull String jarPath, boolean useSpring, IContainerContext containerContext) throws Throwable {
        if (!jarPath.endsWith(".jar")) {
            throw new IllegalArgumentException("Invalid jarPath: " + jarPath);
        }

        long t0 = System.nanoTime();
        log.warn("Loading partner:{} useSpring:{}", jarPath, useSpring);
        try {
            Plugin plugin = new Plugin(code, version, jdkClassLoader, containerClassLoader, containerApplicationContext);
            plugin.load(jarPath, useSpring, Partner.class, containerContext);

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

        /**
         * 加载业务前台jar包，使用默认的IContainerContext实现.
         * <p>
         * <p>如果使用本动态加载，就不要maven里静态引入业务前台jar包依赖了.</p>
         *
         * @param code      {@link IPlugin#getCode()}
         * @param version   version of the jar
         * @param jarPath   jar path
         * @param useSpring jar包里是否需要Spring机制
         * @throws Throwable
         */
    public synchronized void loadPartnerPlugin(@NonNull String code, @NonNull String version, @NonNull String jarPath, boolean useSpring) throws Throwable {
        loadPartnerPlugin(code, version, jarPath, useSpring, new ContainerContext(containerApplicationContext));
    }

    File createLocalFile(@NonNull URL jarUrl) throws IOException {
        String prefix = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String suffix = jarUrl.getPath().substring(jarUrl.getPath().lastIndexOf('/') + 1);
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
