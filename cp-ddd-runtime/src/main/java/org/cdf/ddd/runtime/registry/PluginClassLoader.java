/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.cdf.ddd.runtime.registry;

import lombok.extern.slf4j.Slf4j;
import org.cdf.ddd.annotation.UnderDevelopment;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Plugin类加载器.
 */
@Slf4j
@UnderDevelopment
class PluginClassLoader extends URLClassLoader {
    private static final String dddPackage = "org.cdf.ddd";

    private static ClassLoader jdkClassLoader; // JDK本身的类加载器
    private static ClassLoader containerClassLoader; // 中台容器类加载器
    private static Object mutex = new Object();

    private static PluginClassLoader instance = new PluginClassLoader(new URL[]{});

    PluginClassLoader(URL[] urls) {
        super(urls);

        for (URL url : urls) {
            getInstance().addURL(url);
        }
    }

    static PluginClassLoader getInstance() {
        return instance;
    }

    void addUrl(URL url) {
        super.addURL(url);
    }

    @Override
    protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        Class clazz = this.findLoadedClass(className);
        if (clazz != null) {
            // 如果类已经加载过，就返回那个已经加载好的类
            return clazz;
        }

        // 如果这个类是JDK自己的，就用 JDKClassLoader 加载
        try {
            clazz = jdkClassLoader().loadClass(className);
            if (clazz != null) {
                // 说明该类是JRE的类
                log.debug("JDKClassLoader loaded {}", className);
                return clazz;
            }
        } catch (ClassNotFoundException ignored) {
        }

        // 不是JDK本身的类
        if (containerFirstClass(className)) {
            clazz = containerClassLoader().loadClass(className); // might throw ClassNotFoundException，中台无法加载
            if (clazz != null) {
                log.debug("ContainerClassLoader loaded {}", className);
                return clazz;
            }
        }

        // Plugin加载器加载
        try {
            clazz = this.findClass(className);
            if (clazz != null) {
                log.info("PluginClassLoader loaded {}", className);
                return clazz;
            }
        } catch (ClassNotFoundException ignored) {
        }

        // 如果Plugin加载器无法加载，fallback to 中台Container加载器
        if (clazz == null) {
            clazz = containerClassLoader().loadClass(className); // might throw ClassNotFoundException
            if (clazz != null) {
                log.debug("ContainerClassLoader loaded {}", className);
                return clazz;
            }
        }

        // null
        return clazz;
    }

    // 中台Container优先加载的类
    boolean containerFirstClass(String className) {
        return className != null && className.startsWith(dddPackage);
    }

    static ClassLoader containerClassLoader() {
        if (containerClassLoader != null) {
            return containerClassLoader;
        }

        synchronized (mutex) {
            // double check
            if (containerClassLoader != null) {
                return containerClassLoader;
            }

            containerClassLoader = PluginClassLoader.class.getClassLoader();
            log.debug("containerClassLoader created as parent of PluginClassLoader");
        }

        return containerClassLoader;
    }

    static ClassLoader jdkClassLoader() {
        if (jdkClassLoader != null) {
            return jdkClassLoader;
        }

        synchronized (mutex) {
            // double check
            if (jdkClassLoader != null) {
                return jdkClassLoader;
            }

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
                log.error("jdkClassLoader", shouldNeverHappen);
            }

            jdkClassLoader = new URLClassLoader(jdkUrls.toArray(new URL[0]), parent);
            log.debug("jdkClassLoader created");
        }

        return jdkClassLoader;
    }
}
