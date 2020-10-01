/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.cdf.ddd.runtime.registry;

import org.cdf.ddd.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Component
@Slf4j
class RegistryFactory implements InitializingBean {
    // 有序的，因为他们之间有时间依赖关系
    private static List<RegistryEntry> validRegistryEntries = new ArrayList<>();

    void register(ApplicationContext applicationContext) {
        for (RegistryEntry entry : validRegistryEntries) {
            log.info("register {}'s ...", entry.annotation.getSimpleName());

            for (Object springBean : applicationContext.getBeansWithAnnotation(entry.annotation).values()) {
                entry.createRegistry().registerBean(springBean);
            }
        }

        InternalIndexer.postIndexing();
    }

    static void lazyRegister(Class<? extends Annotation> annotation, Object bean) {
        for (RegistryEntry entry : validRegistryEntries) {
            if (entry.annotation.equals(annotation)) {
                entry.createRegistry().registerBean(bean);
                return;
            }
        }

        throw BootstrapException.ofMessage("Unsupported type: " + annotation.getCanonicalName());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("setup the discoverable Spring beans...");

        // 注册Domain，是为了可视化，避免漏掉某些支撑域
        validRegistryEntries.add(new RegistryEntry(Domain.class, () -> new DomainDef()));
        validRegistryEntries.add(new RegistryEntry(DomainService.class, () -> new DomainServiceDef()));
        validRegistryEntries.add(new RegistryEntry(Step.class, () -> new StepDef()));
        validRegistryEntries.add(new RegistryEntry(DomainAbility.class, () -> new DomainAbilityDef()));
        validRegistryEntries.add(new RegistryEntry(Partner.class, () -> new PartnerDef()));
        validRegistryEntries.add(new RegistryEntry(Pattern.class, () -> new PatternDef()));
        validRegistryEntries.add(new RegistryEntry(Extension.class, () -> new ExtensionDef()));
    }

    private static class RegistryEntry {
        private final Class<? extends Annotation> annotation;
        private final Supplier<IRegistryAware> supplier;

        RegistryEntry(Class<? extends Annotation> annotation, Supplier<IRegistryAware> supplier) {
            this.annotation = annotation;
            this.supplier = supplier;
        }

        IRegistryAware createRegistry() {
            return supplier.get();
        }
    }
}
