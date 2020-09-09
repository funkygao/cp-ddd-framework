/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.ddd.cp.ddd.runtime.registry;

import org.ddd.cp.ddd.annotation.Pattern;
import org.ddd.cp.ddd.ext.IDomainExtension;
import org.ddd.cp.ddd.model.IDomainModel;
import org.ddd.cp.ddd.ext.IIdentityResolver;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
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

    private Map<Class<? extends IDomainExtension>, ExtensionDef> extensionDefMap = new HashMap<>();

    @Override
    public void registerBean(@NotNull Object bean) {
        Pattern pattern = CoreAopUtils.getAnnotation(bean, Pattern.class);
        this.code = pattern.code();
        this.name = pattern.name();
        this.priority = pattern.priority();
        if (this.priority < 0) {
            throw BootstrapException.ofMessage("Patter.priority must be zero or positive");
        }
        if (!(bean instanceof IIdentityResolver)) {
            throw BootstrapException.ofMessage(bean.getClass().getCanonicalName(), " MUST implements IIdentityResolver");
        }
        this.patternBean = (IIdentityResolver) bean;

        InternalIndexer.indexPattern(this);
    }

    @Override
    public boolean match(@NotNull IDomainModel model) {
        return patternBean.match(model);
    }

    void registerExtensionDef(ExtensionDef extensionDef) {
        Class<? extends IDomainExtension> extClazz = extensionDef.getExtClazz();
        if (extensionDefMap.containsKey(extClazz)) {
            throw BootstrapException.ofMessage("Pattern(code=", code, ") can hold ONLY one instance on ", extClazz.getCanonicalName(),
                    ", existing ", extensionDefMap.get(extClazz).toString(),
                    ", illegal ", extensionDef.toString());
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
