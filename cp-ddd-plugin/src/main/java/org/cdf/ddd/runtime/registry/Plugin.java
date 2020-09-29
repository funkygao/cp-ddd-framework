/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.cdf.ddd.runtime.registry;

import lombok.extern.slf4j.Slf4j;
import org.cdf.ddd.annotation.Extension;
import org.cdf.ddd.annotation.Partner;
import org.cdf.ddd.annotation.Pattern;
import org.cdf.ddd.plugin.IContainerContext;
import org.cdf.ddd.plugin.IPluginListener;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
final class Plugin {
    private static final String pluginXml = "/plugin.xml";

    private final ClassLoader jdkClassLoader;
    private final ClassLoader containerClassLoader;
    private ClassLoader pluginClassLoader;

    private ApplicationContext applicationContext;

    Plugin(ClassLoader jdkClassLoader, ClassLoader containerClassLoader) {
        this.jdkClassLoader = jdkClassLoader;
        this.containerClassLoader = containerClassLoader;
    }

    Plugin load(@NotNull String jarPath, String basePackage, Class<? extends Annotation> identityResolverClass, IContainerContext ctx) throws Throwable {
        if (identityResolverClass != Pattern.class && identityResolverClass != Partner.class) {
            throw new IllegalArgumentException("Must be Pattern or Partner");
        }

        // eager load IPlugable classes，通过Java类加载的全盘负责机制自动加载相关引用类
        List<Class<? extends Annotation>> annotations = new ArrayList<>(2);
        annotations.add(identityResolverClass);
        annotations.add(Extension.class);

        // each Plugin Jar has a dedicated PluginClassLoader
        pluginClassLoader = new PluginClassLoader(new URL[]{new File(jarPath).toURI().toURL()},
                jdkClassLoader, containerClassLoader);

        if (basePackage != null && !basePackage.isEmpty()) {
            log.info("Spring loading Plugin with {}, {}, {} ...", jdkClassLoader, containerClassLoader, pluginClassLoader);
            long t0 = System.nanoTime();
            applicationContext = loadSpringApplicationContext(pluginClassLoader);
            log.info("Spring loading cost {}ms", (System.nanoTime() - t0) / 1000_000);
        }

        log.info("prepare Plugin for registry: {}", annotations);
        Map<Class<? extends Annotation>, List<Class>> resultMap = JarUtils.loadClassWithAnnotations(
                jarPath, annotations, null, pluginClassLoader);
        log.debug("prepared: {}", resultMap);

        // IPluginListener 不通过Spring加载
        IPluginListener pluginListener = JarUtils.loadBeanWithType(pluginClassLoader, jarPath, IPluginListener.class);
        if (pluginListener != null) {
            pluginListener.beforeUnload(ctx);
        }

        // 现在，新jar里的类已经被新的ClassLoader加载到内存了，同时旧jar里的类仍然在工作
        // 新jar里的类被IIdentityResolver切换后，新的请求就会发过来；旧的类在in-flight job完成后就不再调用了，最终被GC
        // 注册和切换是在RegistryFactory一并完成的
        // TODO 需要把Extension、IIdentityResolver 的切换过程变成原子的：一个Plugin里可以有多个Pattern，他们的切换可以不必atomic

        log.info("register IIdentityResolver");
        List<Class> identityResolverClasses = resultMap.get(identityResolverClass);
        if (identityResolverClasses != null && !identityResolverClasses.isEmpty()) {
            if (identityResolverClass == Partner.class && identityResolverClasses.size() > 1) {
                throw new RuntimeException("One Partner jar can have at most 1 Partner instance!");
            }

            for (Class irc : identityResolverClasses) {
                log.info("Indexing {}", irc.getCanonicalName());
                // 每次加载，由于 PluginClassLoader 是不同的，irc也是不同的
                Object partnerOrPattern = applicationContext.getBean(irc);
                RegistryFactory.lazyRegister(identityResolverClass, partnerOrPattern);
            }
        }

        log.info("register Extension");
        List<Class> extensions = resultMap.get(Extension.class);
        if (extensions != null && !extensions.isEmpty()) {
            for (Class extensionClazz : extensions) {
                log.info("Indexing {}", extensionClazz.getCanonicalName());
                Object extension = applicationContext.getBean(extensionClazz);
                RegistryFactory.lazyRegister(Extension.class, extension);
            }
        }

        if (pluginListener != null) {
            pluginListener.afterLoad(ctx);
        }

        return this;
    }

    private ApplicationContext loadSpringApplicationContext(@NotNull ClassLoader pluginClassLoader) throws Exception {
        return new ClassPathXmlApplicationContext(new String[]{pluginXml}, DDDBootstrap.applicationContext()) {
            protected void initBeanDefinitionReader(XmlBeanDefinitionReader reader) {
                super.initBeanDefinitionReader(reader);
                reader.setBeanClassLoader(pluginClassLoader);

                setClassLoader(pluginClassLoader); // so that it can find the pluginXml
            }
        };
    }
}
