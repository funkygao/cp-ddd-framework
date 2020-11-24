/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.runtime.registry;

import io.github.dddplus.annotation.Policy;
import io.github.dddplus.ext.IDomainExtension;
import io.github.dddplus.ext.IExtPolicy;
import io.github.dddplus.model.IDomainModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@ToString
@Getter(AccessLevel.PACKAGE)
class PolicyDef implements IRegistryAware {

    private IExtPolicy policyBean;

    private Class<? extends IDomainExtension> extClazz;

    // 该扩展点策略控制的所有扩展点实例，key is extension.code
    private Map<String, ExtensionDef> extensionDefMap = new HashMap<>();

    @Override
    public void registerBean(@NotNull Object bean) {
        initialize(bean);

        InternalIndexer.index(this);
    }

    private void initialize(Object bean) {
        Policy policy = InternalAopUtils.getAnnotation(bean, Policy.class);
        this.extClazz = policy.extClazz();
        if (!(bean instanceof IExtPolicy)) {
            throw BootstrapException.ofMessage(bean.getClass().getCanonicalName(), " MUST implements IExtPolicy");
        }
        this.policyBean = (IExtPolicy) bean;
    }

    void registerExtensionDef(ExtensionDef extensionDef) {
        extensionDefMap.put(extensionDef.getCode(), extensionDef);
    }

    @NotNull
    ExtensionDef getExtension(IDomainModel model) {
        // 根据领域模型，让扩展点定位策略计算目标扩展点code: will never be null
        final String extensionCode = policyBean.extensionCode(model);
        if (extensionCode == null) {
            return null;
        }
        
        return extensionDefMap.get(extensionCode);
    }

    String policyName() {
        return policyBean.getClass().getCanonicalName();
    }
}
