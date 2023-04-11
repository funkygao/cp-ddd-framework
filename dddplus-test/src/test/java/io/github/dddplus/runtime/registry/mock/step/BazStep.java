package io.github.dddplus.runtime.registry.mock.step;

import io.github.dddplus.annotation.Step;
import io.github.dddplus.runtime.registry.mock.exception.FooException;
import io.github.dddplus.runtime.registry.mock.interceptor.DomainProfiler;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Step
@Slf4j
public class BazStep extends SubmitStep {

    @DomainProfiler
    @Override
    public void execute(@NonNull FooModel model) throws FooException {
        log.info("submit: {}", model);

        if (model.isWillSleepLong()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }
    }

    @Override
    public void rollback(@NonNull FooModel model, @NonNull FooException cause) {
        log.info("baz rollback for {}", model);
    }

    @Override
    public String stepCode() {
        return Steps.Submit.BazStep;
    }
}
