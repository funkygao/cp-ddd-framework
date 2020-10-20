/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.runtime.registry;

import io.github.dddplus.annotation.Extension;
import io.github.dddplus.annotation.Partner;
import io.github.dddplus.plugin.IContainerContext;
import io.github.dddplus.plugin.IPlugin;
import io.github.dddplus.plugin.IPluginListener;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
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
    private static final String[] pluginXml = new String[]{"/plugin.xml"};

    @Getter
    private final String code;

    @Getter
    private final String version; // for rollback

    // the shared class loaders
    private final ClassLoader jdkClassLoader;
    private final ClassLoader containerClassLoader;

    // parent of pluginApplicationContext
    private final ApplicationContext containerApplicationContext;

    // each Plugin will have a specific plugin class loader
    private ClassLoader pluginClassLoader;

    // each Plugin will have a specific Spring IoC with the same parent: the Container
    private PluginApplicationContext pluginApplicationContext;

    Plugin(String code, String version, ClassLoader jdkClassLoader, ClassLoader containerClassLoader, ApplicationContext containerApplicationContext) {
        this.code = code;
        this.version = version;
        this.jdkClassLoader = jdkClassLoader;
        this.containerClassLoader = containerClassLoader;
        this.containerApplicationContext = containerApplicationContext;
    }

    void load(String jarPath, boolean useSpring, Class<? extends Annotation> identityResolverClass, IContainerContext ctx) throws Throwable {
        // each Plugin Jar has a specific PluginClassLoader
        pluginClassLoader = new PluginClassLoader(new URL[]{new File(jarPath).toURI().toURL()}, jdkClassLoader, containerClassLoader);

        // Spring load classes in jar
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
    }

    void onDestroy() {
        // 把该Plugin下的所有类的所有引用处理干净，这样才能GC介入
        pluginApplicationContext.close();
    }

    // Spring load all relevant classes in the jar using the new PluginClassLoader
    private Map<Class<? extends Annotation>, List<Class>> prepareClasses(String jarPath, boolean useSpring, Class<? extends Annotation> identityResolverClass) throws Throwable {
        if (useSpring) {
            log.debug("Spring loading Plugin with {}, {}, {} ...", jdkClassLoader, containerClassLoader, pluginClassLoader);
            long t0 = System.nanoTime();

            pluginApplicationContext = new PluginApplicationContext(pluginXml, containerApplicationContext, pluginClassLoader);
            pluginApplicationContext.refresh();

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
                Object partnerOrPattern = pluginApplicationContext.getBean(irc);
                RegistryFactory.preparePlugins(identityResolverClass, partnerOrPattern);
            }
        }

        List<Class> extensions = plugableMap.get(Extension.class);
        if (extensions != null && !extensions.isEmpty()) {
            for (Class extensionClazz : extensions) {
                log.info("Preparing index Extension {}", extensionClazz.getCanonicalName());

                // 这里extensionClazz是扩展点实现的类名 e,g. org.example.bp.oms.isv.extension.DecideStepsExt
                // 而不是 IDecideStepsExt。因此，不必担心getBean异常：一个extensionClazz有多个对象
                Object extension = pluginApplicationContext.getBean(extensionClazz);
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

    @Override
    public String toString() {
        return "Plugin:" + code + ":" + version;
    }
}
