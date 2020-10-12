/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.runtime.registry;

import io.github.dddplus.plugin.IContainerContext;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import io.github.dddplus.annotation.Extension;
import io.github.dddplus.annotation.Partner;
import io.github.dddplus.plugin.IPlugin;
import io.github.dddplus.plugin.IPluginListener;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A Plugin is a dynamic loadable Jar that has a dedicated class loader.
 * <p>
 * <p>Plugin Jar是可以被动态加载的Jar = (Pattern + Extension) | (Partner + Extension)</p>
 */
@Slf4j
class Plugin implements IPlugin {
    private static final String pluginXml = "/plugin.xml";

    @Getter
    private final String code;

    private final ClassLoader jdkClassLoader;
    private final ClassLoader containerClassLoader;
    private ClassLoader pluginClassLoader;

    // each Plugin will have a specific Spring IoC with the same parent: the Container
    private ClassPathXmlApplicationContext applicationContext; // TODO 释放 applicationContext.close()

    Plugin(String code, ClassLoader jdkClassLoader, ClassLoader containerClassLoader) {
        this.code = code;
        this.jdkClassLoader = jdkClassLoader;
        this.containerClassLoader = containerClassLoader;
    }

    Plugin load(String jarPath, boolean useSpring, Class<? extends Annotation> identityResolverClass, IContainerContext ctx) throws Throwable {
        Map<Class<? extends Annotation>, List<Class>> plugableMap = prepareClasses(jarPath, useSpring, identityResolverClass);
        log.info("Classes prepared, plugableMap {}", plugableMap);

        // IPluginListener 不通过Spring加载，而是手工加载、创建实例
        // 如果一个jar里有多个 IPluginListener 实现，只会返回第一个实例
        IPluginListener pluginListener = JarUtils.loadBeanWithType(pluginClassLoader, jarPath, IPluginListener.class);
        if (pluginListener != null) {
            if (pluginListener.getClass().isAnnotationPresent(Component.class)
                    || pluginListener.getClass().isAnnotationPresent(Repository.class)
                    || pluginListener.getClass().isAnnotationPresent(Service.class)) {
                abort("IPluginListener instance cannot be Spring bean!");
            }

            pluginListener.onPrepared(ctx);
        }

        // 现在，新jar里的类已经被新的ClassLoader加载到内存了，也实例化了，但旧jar里的类仍然在工作
        preparePlugins(identityResolverClass, plugableMap);
        log.info("Plugins index prepared for {}", identityResolverClass.getSimpleName());

        // 内存里插件相关索引已准备好，现在切换
        commit(identityResolverClass);
        log.info("Committed: {}", identityResolverClass.getSimpleName());

        if (pluginListener != null) {
            pluginListener.onCommitted(ctx);
        }

        return this;
    }

    // load all relevant classes with the new PluginClassLoader
    private Map<Class<? extends Annotation>, List<Class>> prepareClasses(String jarPath, boolean useSpring, Class<? extends Annotation> identityResolverClass) throws Throwable {
        // each Plugin Jar has a specific PluginClassLoader
        pluginClassLoader = new PluginClassLoader(new URL[]{new File(jarPath).toURI().toURL()}, jdkClassLoader, containerClassLoader);

        if (useSpring) {
            log.debug("Spring loading Plugin with {}, {}, {} ...", jdkClassLoader, containerClassLoader, pluginClassLoader);
            long t0 = System.nanoTime();

            applicationContext = new ClassPathXmlApplicationContext(new String[]{pluginXml}, DDDBootstrap.applicationContext()) {
                protected void initBeanDefinitionReader(XmlBeanDefinitionReader reader) {
                    super.initBeanDefinitionReader(reader);
                    reader.setBeanClassLoader(pluginClassLoader);
                    setClassLoader(pluginClassLoader); // so that it can find the pluginXml within the jar
                }
            };

            log.info("Spring loaded, cost {}ms", (System.nanoTime() - t0) / 1000_000);
        }

        // 从Plugin Jar里把 IPlugable 挑出来，以便更新注册表
        List<Class<? extends Annotation>> annotations = new ArrayList<>(2);
        annotations.add(identityResolverClass);
        annotations.add(Extension.class);
        return JarUtils.loadClassWithAnnotations(jarPath, annotations, null, pluginClassLoader);
    }

    private void preparePlugins(Class<? extends Annotation> identityResolverClass, Map<Class<? extends Annotation>, List<Class>> plugableMap) {
        List<Class> identityResolverClasses = plugableMap.get(identityResolverClass);
        if (identityResolverClasses != null && !identityResolverClasses.isEmpty()) {
            if (identityResolverClass == Partner.class && identityResolverClasses.size() > 1) {
                throw new RuntimeException("One Partner jar can have at most 1 Partner instance!");
            }

            for (Class irc : identityResolverClasses) {
                log.info("Preparing index {} {}", identityResolverClass.getSimpleName(), irc.getCanonicalName());

                // 每次加载，由于 PluginClassLoader 是不同的，irc也不同
                Object partnerOrPattern = applicationContext.getBean(irc);
                RegistryFactory.preparePlugins(identityResolverClass, partnerOrPattern);
            }
        }

        List<Class> extensions = plugableMap.get(Extension.class);
        if (extensions != null && !extensions.isEmpty()) {
            for (Class extensionClazz : extensions) {
                log.info("Preparing index Extension {}", extensionClazz.getCanonicalName());

                // 这里extensionClazz是扩展点实现的类名 e,g. org.example.bp.oms.isv.extension.DecideStepsExt
                // 而不是 IDecideStepsExt。因此，不必担心getBean异常：一个extensionClazz有多个对象
                Object extension = applicationContext.getBean(extensionClazz);
                RegistryFactory.preparePlugins(Extension.class, extension);
            }
        }
    }

    private void commit(Class<? extends Annotation> identityResolverClass) {
        if (identityResolverClass == Partner.class) {
            InternalIndexer.commitPartner();
        }
    }

    private void abort(String message) {
        throw BootstrapException.ofMessage(message);
    }
}
