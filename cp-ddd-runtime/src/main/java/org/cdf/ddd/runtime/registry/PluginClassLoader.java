/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.cdf.ddd.runtime.registry;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Plugin类加载器.
 */
@Slf4j
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
        Class result = this.findLoadedClass(className);
        if (result != null) {
            // 如果该类已经被加载，不重新加载
            return result;
        }

        // 先用JDK加载
        try {
            result = jdkClassLoader().loadClass(className);
            if (result != null) {
                // 说明该类是JRE的类
                log.debug("jdkClassLoader loaded {}", className);
                return result;
            }
        } catch (ClassNotFoundException ignored) {
        }

        // 不是JDK本身的类
        if (containerFirstClass(className)) {
            result = containerClassLoader().loadClass(className); // might throw ClassNotFoundException，中台无法加载
            if (result != null) {
                log.debug("containerClassLoader loaded {}", className);
                return result;
            }
        }

        // Plugin加载器加载
        try {
            result = this.findClass(className);
            if (result != null) {
                log.debug("PluginClassLoader loaded {}", className);
            }
        } catch (ClassNotFoundException ignored) {
        }

        // 如果Plugin加载器无法加载，fallback to 中台Container加载器
        if (result == null) {
            result = containerClassLoader().loadClass(className); // might throw ClassNotFoundException
            log.debug("containerClassLoader loaded {}", className);
        }

        return result;
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
