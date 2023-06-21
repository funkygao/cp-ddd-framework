package io.github.errcase.step;

import io.github.dddplus.annotation.Step;
import io.github.dddplus.runtime.registry.mock.exception.FooException;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import io.github.dddplus.runtime.registry.mock.step.BarStep;
import io.github.dddplus.runtime.registry.mock.step.Steps;
import io.github.dddplus.runtime.registry.mock.step.SubmitStep;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Step(name = "foo活动", tags = Steps.Submit.GoodsValidationGroup)
@Slf4j
public class DupFooStep extends SubmitStep {

    @Override
    public void execute(@NonNull FooModel model) throws FooException {
        log.info("submit: {}", model);
    }

    @Override
    public void rollback(@NonNull FooModel model, @NonNull FooException cause) {
        log.warn("rollback, cause: {}", cause.getMessage());

        if (!cause.getMessage().equals(BarStep.rollbackReason)) {
            throw new RuntimeException("assert fails");
        }

        // rollback里抛出的异常统统被吃掉
        throw new RuntimeException("this exception will be ignored by StepsExecTemplate");
    }

    @Override
    public String stepCode() {
        return Steps.Submit.FooStep;
    }
}
