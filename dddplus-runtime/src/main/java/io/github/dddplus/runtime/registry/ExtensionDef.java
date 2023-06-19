/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.runtime.registry;

import io.github.dddplus.annotation.Extension;
import io.github.dddplus.ext.IDomainExtension;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;

/**
 * 扩展点的内部定义, internal usage only.
 */
@ToString
@Slf4j
public class ExtensionDef implements IRegistryAware, IPrepareAware {
    @Getter
    private String code;

    @Getter
    private String name;

    @Getter
    private Class<? extends IDomainExtension> extClazz;

    @Getter
    private IDomainExtension extensionBean;

    public ExtensionDef() {
    }

    public ExtensionDef(IDomainExtension extensionBean) {
        this.extensionBean = extensionBean;
    }

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
        Extension extension = InternalAopUtils.getAnnotation(bean, Extension.class);
        this.code = extension.code();
        this.name = extension.name();
        if (!(bean instanceof IDomainExtension)) {
            throw BootstrapException.ofMessage(bean.getClass().getCanonicalName(), " MUST implement IDomainExtension");
        }
        this.extensionBean = (IDomainExtension) bean;
        // this.extensionBean might be Xxx$EnhancerBySpringCGLIB if the extension uses AOP
        // 扩展点实现类，可能有继承关系 ClassUtils.getAllInterfaces会取所有(indirect)的接口
        for (Class extensionBeanInterfaceClazz : ClassUtils.getAllInterfaces(InternalAopUtils.getTarget(bean))) {
            if (extensionBeanInterfaceClazz.isInstance(extensionBean)) {
                this.extClazz = extensionBeanInterfaceClazz;

                log.debug("{} has ext instance:{}", this.extClazz.getCanonicalName(), this);
                return;
            }
        }

        // extClazz unset
        throw BootstrapException.ofMessage("Even after many tries, still unable to figure out the extension class of:" + bean.getClass().getCanonicalName());
    }

}
