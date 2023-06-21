/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.runtime.registry;

import io.github.dddplus.annotation.*;
import lombok.extern.slf4j.Slf4j;
import io.github.dddplus.ext.IPlugable;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Component
@Slf4j
class RegistryFactory implements InitializingBean {
    // 有序的，因为他们之间有时间依赖关系
    static List<RegistryEntry> validRegistryEntries = new ArrayList<>();

    private static Map<Class<? extends Annotation>, PrepareEntry> validPrepareEntries = new HashMap<>(3);

    void register(ApplicationContext applicationContext) {
        for (RegistryEntry registryEntry : validRegistryEntries) {
            log.info("register {}'s ...", registryEntry.annotation.getSimpleName());

            for (Object springBean : applicationContext.getBeansWithAnnotation(registryEntry.annotation).values()) {
                registryEntry.create().registerBean(springBean);
            }
        }

        InternalIndexer.postIndexing();
    }

    static void preparePlugins(Class<? extends Annotation> annotation, Object bean) {
        if (!(bean instanceof IPlugable)) {
            throw BootstrapException.ofMessage(bean.getClass().getCanonicalName() + " must be IPlugable");
        }

        PrepareEntry prepareEntry = validPrepareEntries.get(annotation);
        if (prepareEntry == null) {
            throw BootstrapException.ofMessage(annotation.getCanonicalName() + " not supported");
        }

        prepareEntry.create().prepare(bean);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("setup the discoverable Spring beans...");

        validRegistryEntries.add(new RegistryEntry(Domain.class, () -> new DomainDef()));
        validRegistryEntries.add(new RegistryEntry(Interceptor.class, () -> new InterceptorDef()));
        validRegistryEntries.add(new RegistryEntry(DomainService.class, () -> new DomainServiceDef()));
        validRegistryEntries.add(new RegistryEntry(Step.class, () -> new StepDef()));
        validRegistryEntries.add(new RegistryEntry(Router.class, () -> new RouterDef()));
        validRegistryEntries.add(new RegistryEntry(Policy.class, () -> new PolicyDef()));
        validRegistryEntries.add(new RegistryEntry(Partner.class, () -> new PartnerDef()));
        validRegistryEntries.add(new RegistryEntry(Pattern.class, () -> new PatternDef()));
        validRegistryEntries.add(new RegistryEntry(Extension.class, () -> new ExtensionDef()));

        validPrepareEntries.put(Partner.class, new PrepareEntry(() -> new PartnerDef()));
        validPrepareEntries.put(Extension.class, new PrepareEntry(() -> new ExtensionDef()));
    }

    private static class RegistryEntry {
        private final Class<? extends Annotation> annotation;
        private final Supplier<IRegistryAware> supplier;

        RegistryEntry(Class<? extends Annotation> annotation, Supplier<IRegistryAware> supplier) {
            this.annotation = annotation;
            this.supplier = supplier;
        }

        IRegistryAware create() {
            return supplier.get();
        }
    }

    private static class PrepareEntry {
        private final Supplier<IPrepareAware> supplier;

        PrepareEntry(Supplier<IPrepareAware> supplier) {
            this.supplier = supplier;
        }

        IPrepareAware create() {
            return supplier.get();
        }
    }
}
