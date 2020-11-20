package io.github.dddplus.runtime.registry.mock.step;

import io.github.dddplus.annotation.Step;
import io.github.dddplus.runtime.DDD;
import io.github.dddplus.runtime.registry.mock.ability.SleepAbility;
import io.github.dddplus.runtime.registry.mock.exception.FooException;
import io.github.dddplus.runtime.registry.mock.interceptor.DomainProfiler;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;

@Step(name = "foo活动", tags = Steps.Submit.GoodsValidationGroup)
@Slf4j
public class FooStep extends SubmitStep {

    @Override
    @DomainProfiler
    public void execute(@NotNull FooModel model) throws FooException {
        log.info("submit: {}", model);

        if (model.isSleepExtTimeout()) {
            DDD.findAbility(SleepAbility.class).sleepTooLong(model);
        }

        if (model.isWillSleepLong()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }

        if (model.isLetFooThrowException()) {
            log.info("will throw exception!");
            throw new RuntimeException("foo on purpose");
        }
    }

    @Override
    public void rollback(@NotNull FooModel model, @NotNull FooException cause) {
        log.warn("foo rollback, cause: {}", cause.getMessage());

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
