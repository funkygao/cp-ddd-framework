package org.x.cp.ddd.runtime.registry;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.x.cp.ddd.annotation.Extension;
import org.x.cp.ddd.model.IDomainExtension;

import javax.validation.constraints.NotNull;

/**
 * 扩展点的内部定义, internal usage only.
 */
@ToString
@Slf4j
public class ExtensionDef implements IRegistryAware {

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
    public void registerBean(@NotNull Object bean) {
        Extension extension = CoreAopUtils.getAnnotation(bean, Extension.class);
        this.code = extension.code();
        this.name = extension.name();
        if (!(bean instanceof IDomainExtension)) {
            throw BootstrapException.ofMessage(bean.getClass().getCanonicalName(), " MUST implement IDomainExtension");
        }
        this.extensionBean = (IDomainExtension) bean;
        for (Class extensionBeanInterfaceClazz : extensionBean.getClass().getInterfaces()) {
            if (extensionBeanInterfaceClazz.isInstance(extensionBean)) {
                this.extClazz = extensionBeanInterfaceClazz;

                log.info("{} has ext instance:{}", this.extClazz.getCanonicalName(), this);
                break;
            }
        }

        InternalIndexer.indexExtension(this);
    }

}
