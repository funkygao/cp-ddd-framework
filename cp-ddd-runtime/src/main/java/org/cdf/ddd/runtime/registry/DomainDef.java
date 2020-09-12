/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.cdf.ddd.runtime.registry;

import org.cdf.ddd.annotation.Domain;
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
