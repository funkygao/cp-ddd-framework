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
import org.cdf.ddd.annotation.UnderDevelopment;
import org.cdf.ddd.plugin.IContainerContext;
import org.cdf.ddd.plugin.IPluginListener;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.EnvironmentCapable;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@UnderDevelopment
class PluginLoader {

    void load(@NotNull String jarPath, String basePackage, Class<? extends Annotation> identityResolverClass, IContainerContext ctx) throws Exception {
        if (identityResolverClass != Pattern.class && identityResolverClass != Partner.class) {
            throw new IllegalArgumentException("Must be Pattern or Partner");
        }

        List<Class<? extends Annotation>> annotations = new ArrayList<>(2);
        annotations.add(identityResolverClass);
        annotations.add(Extension.class);

        // 个性化业务的ClassLoader，目前是所有业务共享一个 TODO 每个业务单独一个
        PluginClassLoader pluginClassLoader = PluginClassLoader.getInstance();
        pluginClassLoader.addUrl(new File(jarPath).toURI().toURL());

        ApplicationContext applicationContext = DDDBootstrap.applicationContext();

        if (basePackage != null) {
            // 先扫spring，然后初始化所有的basePackage bean，包括已经在中台里加载完的bean
            log.info("Spring scan with {} ...", pluginClassLoader);
            springScanComponent(applicationContext, pluginClassLoader, basePackage);
        }

        log.info("loading classes with annotations: {}", annotations);
        Map<Class<? extends Annotation>, List<Class>> resultMap = JarUtils.loadClassWithAnnotations(
                jarPath, annotations, null, pluginClassLoader);
        log.debug("loaded classes: {}", resultMap);

        IPluginListener pluginListener = JarUtils.loadBeanWithType(pluginClassLoader, jarPath, IPluginListener.class);
        if (pluginListener != null) {
            pluginListener.beforeUnload(ctx);
        }

        // 现在，新jar里的类已经被新的ClassLoader加载到内存了，同时旧jar里的类仍然在工作
        // 新jar里的类被IIdentityResolver切换后，新的请求就会发过来；旧的类在in-flight job完成后就不再调用了，最终被GC
        // 注册和切换是在RegistryFactory一并完成的
        // TODO 需要把Extension、IIdentityResolver 的切换过程变成原子的：一个Plugin里可以有多个Pattern，他们的切换可以不必atomic

        log.info("register and index IIdentityResolver...");
        List<Class> identityResolverClasses = resultMap.get(identityResolverClass);
        if (identityResolverClasses != null && !identityResolverClasses.isEmpty()) {
            if (identityResolverClass == Partner.class && identityResolverClasses.size() > 1) {
                throw new RuntimeException("One Partner jar can have at most 1 Partner instance!");
            }

            for (Class irc : identityResolverClasses) {
                log.info("lazy register {}", irc.getCanonicalName());
                RegistryFactory.lazyRegister(identityResolverClass, applicationContext.getBean(irc));
            }
        }

        log.info("register and index extensions...");
        List<Class> extensions = resultMap.get(Extension.class);
        if (extensions != null && !extensions.isEmpty()) {
            for (Class extensionClazz : extensions) {
                log.info("lazy register {}", extensionClazz.getCanonicalName());
                RegistryFactory.lazyRegister(Extension.class, applicationContext.getBean(extensionClazz));
            }
        }

        if (pluginListener != null) {
            pluginListener.afterLoad(ctx);
        }
    }

    // manual <context:component-scan>
    private void springScanComponent(@NotNull ApplicationContext context, @NotNull ClassLoader pluginClassLoader, @NotNull String... basePackages) throws Exception {
        AbstractRefreshableApplicationContext realContext;
        if (context instanceof ClassPathXmlApplicationContext) {
            realContext = (ClassPathXmlApplicationContext) context;
        } else {
            realContext = (FileSystemXmlApplicationContext) context;
        }

        // 加载该jar包里的Spring bean时，使用该ClassLoader
        realContext.getBeanFactory().setBeanClassLoader(pluginClassLoader);

        BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) realContext.getBeanFactory();
        new ClassPathBeanDefinitionScanner(
                beanDefinitionRegistry,
                true,
                getOrCreateEnvironment(beanDefinitionRegistry),
                new PathMatchingResourcePatternResolver(new DefaultResourceLoader(pluginClassLoader))
        ).scan(basePackages);

        if (false) {
            // 加载业务前台的spring xml
            XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(beanDefinitionRegistry);
            xmlReader.loadBeanDefinitions(new ClassPathResource("spring-main.xml"));
        }
    }

    private Environment getOrCreateEnvironment(BeanDefinitionRegistry registry) {
        Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
        if (registry instanceof EnvironmentCapable) {
            return ((EnvironmentCapable) registry).getEnvironment();
        }

        return new StandardEnvironment();
    }
}
