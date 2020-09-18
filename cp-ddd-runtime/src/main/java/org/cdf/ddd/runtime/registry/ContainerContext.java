package org.cdf.ddd.runtime.registry;

import org.cdf.ddd.plugin.IContainerContext;
import org.springframework.context.ApplicationContext;

import java.lang.annotation.Annotation;

class ContainerContext implements IContainerContext {
    final ApplicationContext applicationContext;

    ContainerContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void registerBean(Class<? extends Annotation> annotation, Object object) throws Exception {
        if (!RegistryFactory.lazyRegister(annotation, object)) {
            throw new RuntimeException("Unsupported annotation: " + annotation.getCanonicalName());
        }
    }

    @Override
    public void deregisterBean(Class<? extends Annotation> annotation, Object object) throws Exception {

    }
}
