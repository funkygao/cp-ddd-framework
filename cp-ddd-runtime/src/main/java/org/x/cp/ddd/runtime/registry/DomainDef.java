package org.x.cp.ddd.runtime.registry;

import org.x.cp.ddd.annotation.Domain;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ToString
class DomainDef implements IRegistryAware {

    @Getter
    private String code;

    @Getter
    private String name;

    @Getter
    private Object domainBean;

    @Override
    public void registerBean(@NotNull Object bean) {
        Domain domain = CoreAopUtils.getAnnotation(bean, Domain.class);
        this.code = domain.code();
        this.name = domain.name();
        this.domainBean = bean;

        InternalIndexer.indexDomain(this);
    }
}
