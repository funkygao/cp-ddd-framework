package org.x.cp.ddd.runtime.registry;

import org.x.cp.ddd.annotation.Pattern;
import org.x.cp.ddd.model.IDomainExtension;
import org.x.cp.ddd.model.IDomainModel;
import org.x.cp.ddd.model.IDomainModelMatcher;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ToString
class PatternDef implements IRegistryAware, IDomainModelMatcher {

    @Getter
    private String code;

    @Getter
    private String name;

    @Getter
    private int priority;

    private IDomainModelMatcher patternBean;

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
        if (!(bean instanceof IDomainModelMatcher)) {
            throw BootstrapException.ofMessage(bean.getClass().getCanonicalName(), " MUST implements IDomainModelMatcher");
        }
        this.patternBean = (IDomainModelMatcher) bean;

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
