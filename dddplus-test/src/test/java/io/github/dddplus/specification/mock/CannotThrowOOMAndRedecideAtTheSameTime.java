package io.github.dddplus.specification.mock;

import io.github.dddplus.runtime.registry.mock.model.FooModel;
import io.github.dddplus.specification.ISpecification;
import io.github.dddplus.specification.Notification;

import javax.validation.constraints.NotNull;

public class CannotThrowOOMAndRedecideAtTheSameTime implements ISpecification<FooModel> {
    public static final String REASON = "不能同时OOM and Redecide";

    @Override
    public boolean satisfiedBy(@NotNull FooModel candidate, Notification notification) {
        if (candidate.isWillThrowOOM() && candidate.isRedecide()) {
            if (notification != null) {
                notification.addReason(REASON);
            }
            return false;
        }

        return true;
    }
}
