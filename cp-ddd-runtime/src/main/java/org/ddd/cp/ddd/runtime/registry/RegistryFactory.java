package org.ddd.cp.ddd.runtime.registry;

import org.ddd.cp.ddd.annotation.*;
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
    private static List<RegistryEntry> supportedRegisters = new ArrayList<>();

    void register(ApplicationContext applicationContext) {
        for (RegistryEntry entry : supportedRegisters) {
            log.info("register {}'s ...", entry.annotation.getSimpleName());

            for (Object springBean : applicationContext.getBeansWithAnnotation(entry.annotation).values()) {
                entry.createRegistry().registerBean(CoreAopUtils.getTarget(springBean));
            }
        }

        InternalIndexer.postIndexing();
    }

    static boolean lazyRegister(Class<? extends Annotation> annotation, Object bean) {
        for (RegistryEntry entry : supportedRegisters) {
            if (entry.annotation.equals(annotation)) {
                // bingo!
                entry.createRegistry().registerBean(CoreAopUtils.getTarget(bean));
                return true;
            }
        }

        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("setup the discoverable Spring beans...");

        // 注册Domain，是为了可视化，避免漏掉某些支撑域
        supportedRegisters.add(new RegistryEntry(Domain.class, () -> new DomainDef()));
        supportedRegisters.add(new RegistryEntry(DomainService.class, () -> new DomainServiceDef()));
        supportedRegisters.add(new RegistryEntry(Step.class, () -> new StepDef()));
        supportedRegisters.add(new RegistryEntry(DomainAbility.class, () -> new DomainAbilityDef()));
        supportedRegisters.add(new RegistryEntry(Partner.class, () -> new PartnerDef()));
        supportedRegisters.add(new RegistryEntry(Pattern.class, () -> new PatternDef()));
        supportedRegisters.add(new RegistryEntry(Extension.class, () -> new ExtensionDef()));
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
