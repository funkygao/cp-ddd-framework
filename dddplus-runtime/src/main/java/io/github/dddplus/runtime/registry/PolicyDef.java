/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.runtime.registry;

import io.github.dddplus.ext.IDomainExtension;
import io.github.dddplus.ext.IPolicy;
import io.github.dddplus.ext.IIdentity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;

import java.util.HashMap;
import java.util.Map;

@ToString
@Getter(AccessLevel.PACKAGE)
@Slf4j
class PolicyDef implements IRegistryAware {
    private static final int MAX_INHERITANCE_DEPTH = 5;

    private IPolicy policyBean;

    private Class<? extends IDomainExtension> extClazz;
    private Class<? extends IPolicy> policyClazz;

    // 该扩展点策略控制的所有扩展点实例，key is extension.code
    private Map<String, ExtensionDef> extensionDefMap = new HashMap<>();

    @Override
    public void registerBean(@NonNull Object bean) {
        initialize(bean);

        resolveExtClazz();
        log.debug("policy:{} ext:{}", bean.getClass().getCanonicalName(), extClazz.getCanonicalName());

        InternalIndexer.index(this);
    }

    private void initialize(Object bean) {
        if (!(bean instanceof IPolicy)) {
            throw BootstrapException.ofMessage(bean.getClass().getCanonicalName(), " MUST implements IPolicy");
        }
        this.policyBean = (IPolicy) bean;
        this.policyClazz = (Class<? extends IPolicy>) InternalAopUtils.getTarget(bean).getClass();
    }

    private void resolveExtClazz() {
        // 实现IPolicy的类可能有继承：
        // abstract class AbstractFooPolicy implements IPolicy<IFooExt, Ident>
        // class ConcreteFooPolicy extends AbstractFooPolicy
        ResolvableType resolvableType = ResolvableType.forInstance(InternalAopUtils.getTarget(this.policyBean));
        for (int i = 0; i < MAX_INHERITANCE_DEPTH; i++) {
            for (ResolvableType iPolicyType : resolvableType.getInterfaces()) {
                if (IPolicy.class.isAssignableFrom(iPolicyType.resolve())) {
                    // bingo!
                    ResolvableType extType = iPolicyType.getGeneric(0); // IPolicy的第一个泛型是扩展点类型
                    this.extClazz = (Class<? extends IDomainExtension>) extType.resolve();
                    return;
                }
            }

            // 向父类找
            resolvableType = resolvableType.getSuperType();
        }

        throw BootstrapException.ofMessage("Even after many tries, still unable to figure out the extension class of IPolicy:", this.policyClazz.getCanonicalName());
    }

    void registerExtensionDef(ExtensionDef extensionDef) {
        extensionDefMap.put(extensionDef.getCode(), extensionDef);
    }

    @NonNull
    ExtensionDef getExtension(IIdentity identity) {
        // 根据领域模型，让扩展点定位策略计算目标扩展点code: will never be null
        final String extensionCode = policyBean.extensionCode(identity);
        if (extensionCode == null) {
            return null;
        }

        return extensionDefMap.get(extensionCode);
    }

    String policyName() {
        return policyBean.getClass().getCanonicalName();
    }
}
