package io.github.dddplus.specification.mock;

import io.github.dddplus.annotation.Specification;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import io.github.dddplus.specification.ISpecification;
import io.github.dddplus.specification.Notification;

import javax.validation.constraints.NotNull;

@Specification(name = "B2C业务必须要传递partnerCode")
public class B2CMustHavePartnerCode implements ISpecification<FooModel> {

    @Override
    public boolean isSatisfiedBy(@NotNull FooModel candidate, Notification notification) {
        if (candidate.isB2c()) {
            if (candidate.getPartnerCode() == null || candidate.getPartnerCode().isEmpty()) {
                return false;
            }
        }

        return true;
    }
}
