/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.runtime.registry;

import io.github.dddplus.annotation.Partner;
import io.github.dddplus.ext.IDomainExtension;
import io.github.dddplus.ext.IIdentityResolver;
import io.github.dddplus.ext.IIdentity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
class PartnerDef implements IRegistryAware, IPrepareAware, IIdentityResolver {

    @Getter
    private String code;

    @Getter
    private String name;

    @Getter
    private IIdentityResolver partnerBean;

    @Getter(AccessLevel.PACKAGE)
    private Map<Class<? extends IDomainExtension>, ExtensionDef> extensionDefMap = new HashMap<>();

    @Override
    public void registerBean(@NonNull Object bean) {
        initialize(bean);

        InternalIndexer.index(this);
    }

    @Override
    public void prepare(@NonNull Object bean) {
        initialize(bean);

        InternalIndexer.prepare(this);
    }

    private void initialize(Object bean) {
        Partner partner = InternalAopUtils.getAnnotation(bean, Partner.class);
        this.code = partner.code();
        this.name = partner.name();

        if (!(bean instanceof IIdentityResolver)) {
            throw BootstrapException.ofMessage(bean.getClass().getCanonicalName(), " MUST implements IIdentityResolver");
        }
        this.partnerBean = (IIdentityResolver) bean;
    }

    void registerExtensionDef(ExtensionDef extensionDef) {
        Class<? extends IDomainExtension> extClazz = extensionDef.getExtClazz();
        if (extensionDefMap.containsKey(extClazz)) {
            throw BootstrapException.ofMessage("Partner(code=", code, ") can hold ONLY one instance on ", extClazz.getCanonicalName(),
                    ", existing ", extensionDefMap.get(extClazz).toString(), ", illegal ", extensionDef.toString());
        }

        extensionDefMap.put(extClazz, extensionDef);
    }

    ExtensionDef getExtension(Class<? extends IDomainExtension> extClazz) {
        return extensionDefMap.get(extClazz);
    }

    @Override
    public boolean match(@NonNull IIdentity identity) {
        return partnerBean.match(identity);
    }

}
