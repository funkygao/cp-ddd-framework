package org.x.cp.ddd.runtime.registry;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * 前台类加载器.
 */
@Slf4j
class PartnerClassLoader extends URLClassLoader {
    private static final String dddPackage = "org.x.cp.ddd";

    private static ClassLoader jdkClassLoader; // JDK本身的类加载器
    private static ClassLoader platformClassLoader; // 中台类加载器
    private static Object mutex = new Object();

    PartnerClassLoader(URL[] urls) {
        super(urls);
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
                log.info("jdkClassLoader loaded {}", className);
                return result;
            }
        } catch (ClassNotFoundException ignored) {
        }

        // 不是JDK本身的类
        if (platformFirstClass(className)) {
            result = platformClassLoader().loadClass(className); // might throw ClassNotFoundException，中台无法加载
            if (result != null) {
                log.info("platformClassLoader loaded {}", className);
                return result;
            }
        }

        // 前台加载器加载
        try {
            result = this.findClass(className);
            if (result != null) {
                log.info("projectClassLoader loaded {}", className);
            }
        } catch (ClassNotFoundException ignored) {
        }

        // 如果前台无法加载，fallback to 中台加载器
        if (result == null) {
            result = platformClassLoader().loadClass(className); // might throw ClassNotFoundException
            log.info("platformClassLoader loaded {}", className);
        }

        return result;
    }

    // 中台优先加载的类
    boolean platformFirstClass(String className) {
        return className != null && className.startsWith(dddPackage);
    }

    static ClassLoader platformClassLoader() {
        if (platformClassLoader == null) {
            synchronized (mutex) {
                if (platformClassLoader == null) {
                    // 中台类加载器加载了前台的类加载器
                    platformClassLoader = PartnerClassLoader.class.getClassLoader();
                    log.info("platformClassLoader created");
                }
            }
        }

        return platformClassLoader;
    }

    static ClassLoader jdkClassLoader() {
        if (jdkClassLoader == null) {
            synchronized (mutex) {
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
                log.info("jdkClassLoader created");
            }
        }

        return jdkClassLoader;
    }

}
