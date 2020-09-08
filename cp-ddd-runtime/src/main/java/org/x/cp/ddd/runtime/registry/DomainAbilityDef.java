package org.x.cp.ddd.runtime.registry;

import org.x.cp.ddd.annotation.DomainAbility;
import org.x.cp.ddd.model.BaseDomainAbility;
import org.x.cp.ddd.ext.IDomainExtension;
import org.x.cp.ddd.model.IDomainModel;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@ToString
@Slf4j
class DomainAbilityDef implements IRegistryAware {

    @Getter
    private String domain;

    @Getter
    private String name;

    @Getter
    private BaseDomainAbility domainAbilityBean;

    @Getter
    private Class<? extends BaseDomainAbility> domainAbilityClass;

    @Getter
    private Class<? extends IDomainModel> modelClazz;

    @Getter
    private Class<? extends IDomainExtension> extClazz;

    @Override
    public void registerBean(@NotNull Object bean) {
        DomainAbility domainAbility = CoreAopUtils.getAnnotation(bean, DomainAbility.class);
        this.domain = domainAbility.domain();
        this.name = domainAbility.name();
        if (!(bean instanceof BaseDomainAbility)) {
            throw BootstrapException.ofMessage(bean.getClass().getCanonicalName(), " MUST extend BaseDomainAbility");
        }

        this.domainAbilityBean = (BaseDomainAbility) bean;
        this.domainAbilityClass = (Class<? extends BaseDomainAbility>) CoreAopUtils.getTarget(bean).getClass();

        // 获取BaseDomainAbility的Model和Ext类型
        Type type = CoreAopUtils.getGenericSuperclass(bean);
        if (type == null || !(type instanceof ParameterizedType)) {
            throw BootstrapException.ofMessage("cannot find ParameterizedType for:", bean.getClass().getCanonicalName());
        }
        ParameterizedType parameterizedType = (ParameterizedType) type;
        if (parameterizedType == null || parameterizedType.getActualTypeArguments().length < 2) {
            throw BootstrapException.ofMessage("illegal generic declaration for:", bean.getClass().getCanonicalName());
        }

        this.modelClazz = (Class<? extends IDomainModel>) parameterizedType.getActualTypeArguments()[0];
        this.extClazz = (Class<? extends IDomainExtension>) parameterizedType.getActualTypeArguments()[1];
        log.debug("domain ability:{} model:{} ext:{}", bean.getClass().getCanonicalName(), modelClazz.getCanonicalName(), extClazz.getCanonicalName());

        InternalIndexer.indexDomainAbility(this);
    }
}
