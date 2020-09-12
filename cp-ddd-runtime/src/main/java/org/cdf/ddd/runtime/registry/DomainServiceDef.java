/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.cdf.ddd.runtime.registry;

import org.cdf.ddd.annotation.DomainService;
import org.cdf.ddd.model.IDomainService;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ToString
class DomainServiceDef implements IRegistryAware {

    @Getter
    private String domain;

    @Getter
    private IDomainService domainServiceBean;

    @Override
    public void registerBean(@NotNull Object bean) {
        DomainService domainService = CoreAopUtils.getAnnotation(bean, DomainService.class);
        if (!(bean instanceof IDomainService)) {
            throw BootstrapException.ofMessage(bean.getClass().getCanonicalName(), " MUST implement IDomainService");
        }

        this.domain = domainService.domain();
        this.domainServiceBean = (IDomainService) bean;

        InternalIndexer.indexDomainService(this);
    }
}
