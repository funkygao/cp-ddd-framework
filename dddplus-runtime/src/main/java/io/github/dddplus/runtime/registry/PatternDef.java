/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.runtime.registry;

import io.github.dddplus.annotation.Pattern;
import io.github.dddplus.ext.IDomainExtension;
import io.github.dddplus.ext.IPatternFilter;
import io.github.dddplus.ext.IIdentityResolver;
import io.github.dddplus.ext.IIdentity;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ToString
class PatternDef implements IRegistryAware, IIdentityResolver {

    @Getter
    private String code;

    @Getter
    private String name;

    @Getter
    private int priority;

    private IIdentityResolver patternBean;
    private IPatternFilter filterBean;

    private Map<Class<? extends IDomainExtension>, ExtensionDef> extensionDefMap = new HashMap<>();

    @Override
    public void registerBean(@NonNull Object bean) {
        boolean needIndex = initialize(bean);
        if (needIndex) {
            InternalIndexer.index(this);
        }
    }

    @Override
    public boolean match(@NonNull IIdentity identity) {
        return patternBean.match(identity);
    }

    private boolean initialize(Object bean) {
        Pattern pattern = InternalAopUtils.getAnnotation(bean, Pattern.class);
        this.code = pattern.code();
        this.name = pattern.name();
        this.priority = pattern.priority();
        if (this.priority < 0) {
            throw BootstrapException.ofMessage("Pattern.priority must be zero or positive");
        }

        if (!pattern.asResolver()) {
            // 无需索引，marker only
            return false;
        }

        if (!(bean instanceof IIdentityResolver)) {
            throw BootstrapException.ofMessage(bean.getClass().getCanonicalName(), " MUST implements IIdentityResolver");
        }
        this.patternBean = (IIdentityResolver) bean;
        if (bean instanceof IPatternFilter) {
            this.filterBean = (IPatternFilter) bean;
        }
        return true;
    }

    void registerExtensionDef(ExtensionDef extensionDef) {
        Class<? extends IDomainExtension> extClazz = extensionDef.getExtClazz();
        if (extensionDefMap.containsKey(extClazz)) {
            throw BootstrapException.ofMessage("Pattern(code=", code, ") can hold ONLY one instance on ", extClazz.getCanonicalName(),
                    ", existing ", extensionDefMap.get(extClazz).toString(), ", illegal ", extensionDef.toString());
        }

        extensionDefMap.put(extClazz, extensionDef);
    }

    ExtensionDef getExtension(Class<? extends IDomainExtension> extClazz) {
        return extensionDefMap.get(extClazz);
    }

    Set<Class<? extends IDomainExtension>> extClazzSet() {
        return extensionDefMap.keySet();
    }

}
