/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.runtime.registry;

import io.github.dddplus.annotation.Router;
import io.github.dddplus.ext.IDomainExtension;
import io.github.dddplus.runtime.BaseRouter;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;

@ToString
@Slf4j
class RouterDef implements IRegistryAware {
    private static final int MAX_INHERITANCE_DEPTH = 5;

    @Getter
    private String domain;

    @Getter
    private String name;

    @Getter
    private BaseRouter baseRouterBean;

    @Getter
    private Class<? extends BaseRouter> baseRouterClazz;

    @Getter
    private Class<? extends IDomainExtension> extClazz;

    @Override
    public void registerBean(@NonNull Object bean) {
        Router router = InternalAopUtils.getAnnotation(bean, Router.class);
        this.domain = router.domain();
        this.name = router.name();
        if (!(bean instanceof BaseRouter)) {
            throw BootstrapException.ofMessage(bean.getClass().getCanonicalName(), " MUST extend BaseRouter");
        }

        this.baseRouterBean = (BaseRouter) bean;
        this.baseRouterClazz = (Class<? extends BaseRouter>) InternalAopUtils.getTarget(bean).getClass();

        this.resolveExtClazz();
        log.debug("router:{} ext:{}", bean.getClass().getCanonicalName(), extClazz.getCanonicalName());

        InternalIndexer.index(this);
    }

    private void resolveExtClazz() {
        ResolvableType baseRouterClazz = ResolvableType.forClass(this.baseRouterClazz).getSuperType();
        for (int i = 0; i < MAX_INHERITANCE_DEPTH; i++) {
            for (ResolvableType resolvableType : baseRouterClazz.getGenerics()) {
                if (IDomainExtension.class.isAssignableFrom(resolvableType.resolve())) {
                    this.extClazz = (Class<? extends IDomainExtension>) resolvableType.resolve();
                    return;
                }
            }

            // parent class
            baseRouterClazz = baseRouterClazz.getSuperType();
        }

        // should never happen: otherwise java cannot compile
        throw BootstrapException.ofMessage("Even after many tries, still unable to figure out the extension class of BaseRouter:", this.baseRouterClazz.getCanonicalName());
    }
}
