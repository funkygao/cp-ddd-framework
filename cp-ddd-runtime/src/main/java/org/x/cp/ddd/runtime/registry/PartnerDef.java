package org.x.cp.ddd.runtime.registry;

import org.x.cp.ddd.annotation.Partner;
import org.x.cp.ddd.model.IDomainExtension;
import org.x.cp.ddd.model.IDomainModel;
import org.x.cp.ddd.model.IDomainModelMatcher;
import org.x.cp.ddd.model.IPartnerResolver;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@ToString
class PartnerDef implements IRegistryAware, IDomainModelMatcher {

    @Getter
    private String code;

    @Getter
    private String name;

    @Getter
    private IPartnerResolver resolver;

    private Map<Class<? extends IDomainExtension>, ExtensionDef> extensionDefMap = new HashMap<>();

    @Override
    public void registerBean(@NotNull Object bean) {
        Partner partner = CoreAopUtils.getAnnotation(bean, Partner.class);
        this.code = partner.code();
        this.name = partner.name();

        try {
            // 每个project的resolver都是单例的
            this.resolver = partner.resolverClass().newInstance();
        } catch (InstantiationException shouldNeverHappenBug) {
            // IPartnerResolver 的实现类的默认构造方法被研发故意禁掉了
            throw BootstrapException.ofMessage(shouldNeverHappenBug.getMessage());
        } catch (IllegalAccessException shouldNeverHappenBug) {
            throw BootstrapException.ofMessage(shouldNeverHappenBug.getMessage());
        }

        InternalIndexer.indexPartner(this);
    }

    void registerExtensionDef(ExtensionDef extensionDef) {
        Class<? extends IDomainExtension> extClazz = extensionDef.getExtClazz();
        if (extensionDefMap.containsKey(extClazz)) {
            throw BootstrapException.ofMessage("Partner(code=", code, ") can hold ONLY one instance on ", extClazz.getCanonicalName(),
                    ", existing ", extensionDefMap.get(extClazz).toString(),
                    ", illegal ", extensionDef.toString());
        }

        extensionDefMap.put(extClazz, extensionDef);
    }

    ExtensionDef getExtension(Class<? extends IDomainExtension> extClazz) {
        return extensionDefMap.get(extClazz);
    }

    @Override
    public boolean match(@NotNull IDomainModel model) {
        return resolver.match(model);
    }

}
