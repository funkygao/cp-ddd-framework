package io.github.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Autowired
    private AutowireCapableBeanFactory autowireCapableBeanFactory;
    @Autowired
    private DefaultListableBeanFactory listableBeanFactory;

    private void demo(Object bean) {
        listableBeanFactory.registerSingleton("foo", bean);
        autowireCapableBeanFactory.autowireBean(bean);
    }

    // Option1: by class
    public static <T> T newAutowiredInstance(Class<T> beanClass) {
        return (T) applicationContext.getAutowireCapableBeanFactory()
                .autowire(beanClass, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
    }

    // Option2: by object
    public static <T> T autowire(T bean) {
        applicationContext.getAutowireCapableBeanFactory()
                .autowireBeanProperties(bean, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtil.applicationContext = applicationContext;
    }
}
