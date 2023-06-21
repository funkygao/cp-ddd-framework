/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.runtime.registry;

import io.github.dddplus.annotation.Step;
import io.github.dddplus.step.IDomainStep;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * 领域步骤的内部定义, internal usage only.
 */
@ToString
@Deprecated
public class StepDef implements IRegistryAware {

    @Getter
    private String activity;
    
    @Getter
    private String code;

    @Getter
    private String name;

    @Getter
    private String[] tags;

    @Getter
    private IDomainStep stepBean;

    @Override
    public void registerBean(@NonNull Object bean) {
        Step domainStep = InternalAopUtils.getAnnotation(bean, Step.class);
        this.name = domainStep.name();
        this.tags = domainStep.tags();

        if (!(bean instanceof IDomainStep)) {
            throw BootstrapException.ofMessage(bean.getClass().getCanonicalName(), " MUST implement IDomainStep");
        }
        this.stepBean = (IDomainStep) bean;
        this.activity = this.stepBean.activityCode();
        this.code = this.stepBean.stepCode();
        if (this.activity == null || this.activity.trim().isEmpty()) {
            throw BootstrapException.ofMessage(bean.getClass().getCanonicalName(), " activityCode cannot be empty");
        }
        if (this.code == null || this.code.trim().isEmpty()) {
            throw BootstrapException.ofMessage(bean.getClass().getCanonicalName(), " stepCode cannot be empty");
        }

        InternalIndexer.index(this);
    }
}
