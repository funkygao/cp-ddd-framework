package org.cdf.ddd.runtime.registry;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

// Spring 手动注册 bean
class SpringBeanRegisterUtil {

    private static ConfigurableApplicationContext applicationContext() {
        return (ConfigurableApplicationContext) DDDBootstrap.applicationContext();
    }

    private static BeanDefinitionRegistry beanDefinitionRegistry() {
        return (DefaultListableBeanFactory) applicationContext().getBeanFactory();
    }

    static void registerBean(String beanId, String className) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(className);
        BeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
        beanDefinitionRegistry().registerBeanDefinition(beanId, beanDefinition);
    }

    static void unregisterBean(String beanId) {
        beanDefinitionRegistry().removeBeanDefinition(beanId);
    }

    static <T> T getBean(String name) {
        return (T) applicationContext().getBean(name);
    }
}
