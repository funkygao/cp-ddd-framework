/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.cdf.ddd.runtime.registry;

import lombok.extern.slf4j.Slf4j;
import org.cdf.ddd.annotation.UnderDevelopment;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Plugin类加载器.
 * <p>
 * <pre>
 *     sun.misc.Launcher$AppClassLoader
 *                   |
 *        +--------------------+
 *        |                    |
 *  JDKClassLoader   ContainerClassLoader($AppClassLoader)
 *                             |
 *      +------------------------------------------------+
 *      |                         |                      |
 *  PluginClassLoader      PluginClassLoader      PluginClassLoader
 *      |                         |                      |
 *  PluginJar                 PluginJar               PluginJar
 * </pre>
 */
@Slf4j
@UnderDevelopment
class PluginClassLoader extends URLClassLoader {
    private static final String dddPackage = "org.cdf.ddd";

    private ClassLoader jdkClassLoader; // TODO final
    private ClassLoader containerClassLoader;

    private static PluginClassLoader instance = new PluginClassLoader(new URL[]{});

    PluginClassLoader(URL[] urls) {
        super(urls);

        for (URL url : urls) {
            addUrl(url);
        }
    }

    @Deprecated
    static PluginClassLoader getInstance() {
        return instance;
    }

    void addUrl(URL url) {
        super.addURL(url);
    }

    PluginClassLoader inject(ClassLoader jdkClassLoader, ClassLoader containerClassLoader) {
        this.jdkClassLoader = jdkClassLoader;
        this.containerClassLoader = containerClassLoader;
        return this;
    }

    @Override // TODO resolve
    protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        Class clazz = this.findLoadedClass(className);
        if (clazz != null) {
            // 如果类已经加载过，就返回那个已经加载好的类
            return clazz;
        }

        // 如果这个类是JDK自己的，就用 JDKClassLoader 加载
        try {
            clazz = jdkClassLoader.loadClass(className);
            if (clazz != null) {
                // 说明该类是JRE的类
                log.debug("JDKClassLoader loaded {}", className);
                return clazz;
            }
        } catch (ClassNotFoundException ignored) {
        }

        // 不是JDK本身的类
        if (containerFirstClass(className)) {
            clazz = containerClassLoader.loadClass(className); // parent.loadClass
            if (clazz != null) {
                log.debug("ContainerClassLoader loaded {}", className);
                return clazz;
            }
        }

        // Plugin加载器自己加载
        try {
            // look for classes in the file system(jar)
            clazz = this.findClass(className);
            if (clazz != null) {
                log.info("PluginClassLoader loaded {}", className);
                return clazz;
            }
        } catch (ClassNotFoundException ignored) {
        }

        // 如果Plugin加载器无法加载，fallback to 中台Container加载器
        if (clazz == null) {
            clazz = containerClassLoader.loadClass(className); // might throw ClassNotFoundException
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
}
