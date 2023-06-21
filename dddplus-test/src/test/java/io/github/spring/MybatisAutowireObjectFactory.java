package io.github.spring;

import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.List;

// mybatis-config.xml
// <objectFactory type="io.github.spring.MybatisAutowireObjectFactory" />
// 这样，HasMany/HasOne 在 infrastructure 的实现类就可以自动注入其他对象，例如DAO
@Component
public class MybatisAutowireObjectFactory extends DefaultObjectFactory implements ApplicationContextAware {
    private ApplicationContext context;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = context;
    }

    @Override
    public <T> T create(Class<T> type) {
        T object = super.create(type);
        context.getAutowireCapableBeanFactory().autowireBean(object);
        return object;
    }

    @Override
    public <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
        T object = super.create(type, constructorArgTypes, constructorArgs);
        context.getAutowireCapableBeanFactory().autowireBean(object);
        return object;
    }
}
