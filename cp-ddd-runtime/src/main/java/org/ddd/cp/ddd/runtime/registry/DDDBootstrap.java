/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.ddd.cp.ddd.runtime.registry;

import org.ddd.cp.ddd.runtime.IStartupListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * DDD框架启动器，internal only.
 */
@Component
@Slf4j
class DDDBootstrap implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {
    private static AtomicBoolean once = new AtomicBoolean();

    @Resource
    private RegistryFactory registryFactory;

    @Autowired(required = false)
    private IStartupListener startupListener;

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (!once.compareAndSet(false, true)) {
            log.warn("register more than once, ignored!");
            return;
        }

        long t0 = System.nanoTime();
        log.info("starting Spring, register DDD beans...");
        registryFactory.register(applicationContext);
        log.info("all DDD beans registered, cost {}ms", (System.nanoTime() - t0) / 1000000);

        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            log.info("Spring started complete!");
        } else {
            log.info("Spring reloaded complete!");
        }

        if (startupListener != null) {
            log.info("calling IStartupListener: {}", startupListener.getClass().getCanonicalName());
            startupListener.onStartComplete();
            log.info("called IStartupListener: {}", startupListener.getClass().getCanonicalName());
        }
    }

    static ApplicationContext applicationContext() {
        return applicationContext;
    }

}
