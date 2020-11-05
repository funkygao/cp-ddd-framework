package io.github.badcase.specification;

import io.github.dddplus.runtime.registry.mock.model.FooModel;
import io.github.dddplus.specification.ISpecification;
import io.github.dddplus.specification.Notification;

import javax.validation.constraints.NotNull;

public class SpecificationWithoutAnnotation implements ISpecification<FooModel> {
    @Override
    public boolean satisfiedBy(@NotNull FooModel candidate, Notification notification) {
        return false;
    }
}
