/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.runtime.registry;

import io.github.dddplus.annotation.DomainAbility;
import io.github.dddplus.ext.IDomainExtension;
import io.github.dddplus.runtime.BaseDomainAbility;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;

import javax.validation.constraints.NotNull;

@ToString
@Slf4j
class DomainAbilityDef implements IRegistryAware {

    @Getter
    private String domain;

    @Getter
    private String name;

    @Getter
    private BaseDomainAbility domainAbilityBean;

    @Getter
    private Class<? extends BaseDomainAbility> domainAbilityClass;

    @Getter
    private Class<? extends IDomainExtension> extClazz;

    @Override
    public void registerBean(@NotNull Object bean) {
        DomainAbility domainAbility = InternalAopUtils.getAnnotation(bean, DomainAbility.class);
        this.domain = domainAbility.domain();
        this.name = domainAbility.name();
        if (!(bean instanceof BaseDomainAbility)) {
            throw BootstrapException.ofMessage(bean.getClass().getCanonicalName(), " MUST extend BaseDomainAbility");
        }

        this.domainAbilityBean = (BaseDomainAbility) bean;
        this.domainAbilityClass = (Class<? extends BaseDomainAbility>) InternalAopUtils.getTarget(bean).getClass();

        this.resolveExtClazz();
        log.debug("domain ability:{} ext:{}", bean.getClass().getCanonicalName(), extClazz.getCanonicalName());

        InternalIndexer.index(this);
    }

    private void resolveExtClazz() {
        ResolvableType baseDomainAbilityType = ResolvableType.forClass(this.domainAbilityClass).getSuperType();
        for (int i = 0; i < 10; i++) {
            for (ResolvableType resolvableType : baseDomainAbilityType.getGenerics()) {
                if (IDomainExtension.class.isAssignableFrom(resolvableType.resolve())) {
                    this.extClazz = (Class<? extends IDomainExtension>) resolvableType.resolve();
                    return;
                }
            }

            // parent class
            baseDomainAbilityType = baseDomainAbilityType.getSuperType();
        }

        // should never happen: otherwise java cannot compile
        throw BootstrapException.ofMessage("Even after 10 tries, still unable to figure out the extension class of BaseDomainAbility:", this.domainAbilityClass.getCanonicalName());
    }
}
