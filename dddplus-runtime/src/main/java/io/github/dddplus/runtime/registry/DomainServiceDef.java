/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.runtime.registry;

import io.github.dddplus.annotation.DomainService;
import io.github.dddplus.model.IDomainService;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@ToString
class DomainServiceDef implements IRegistryAware {

    @Getter
    private String domain;

    @Getter
    private IDomainService domainServiceBean;

    @Override
    public void registerBean(@NonNull Object bean) {
        DomainService domainService = InternalAopUtils.getAnnotation(bean, DomainService.class);
        if (!(bean instanceof IDomainService)) {
            throw BootstrapException.ofMessage(bean.getClass().getCanonicalName(), " MUST implement IDomainService");
        }

        this.domain = domainService.domain();
        this.domainServiceBean = (IDomainService) bean;

        InternalIndexer.index(this);
    }
}
