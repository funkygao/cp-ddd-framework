package io.github.dddplus.runtime.registry.mock.step;

import io.github.dddplus.annotation.Step;
import io.github.dddplus.runtime.DDD;
import io.github.dddplus.runtime.registry.mock.exception.FooException;
import io.github.dddplus.runtime.registry.mock.exception.FooReviseStepsException;
import io.github.dddplus.runtime.registry.mock.interceptor.DomainProfiler;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import io.github.dddplus.runtime.registry.mock.router.ReviseStepsRouter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Step(tags = Steps.Submit.GoodsValidationGroup, dependsOn = FooStep.class)
@Slf4j
public class BarStep extends SubmitStep {
    public static final String rollbackReason = "rollback on purpose";

    @DomainProfiler
    @Override
    public void execute(@NonNull FooModel model) throws FooException {
        log.debug("submit: {}", model);

        if (model.isWillSleepLong()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }

        if (model.isRedecideDeadLoop()) {
            // 故意制造step死循环
            log.debug("dead loop on purpose for step:{}", this.stepCode());
            List<String> revisedSteps = new ArrayList<>();
            revisedSteps.add(this.stepCode());
            throw new FooReviseStepsException().withSubsequentSteps(revisedSteps);
        }

        List<String> revisedSteps = DDD.useRouter(ReviseStepsRouter.class).revisedSteps(model);
        if (revisedSteps != null && !revisedSteps.isEmpty()) {
            log.info("重新编排步骤，增加步骤：{}", revisedSteps);

            // 通过异常，来改变后续步骤
            throw new FooReviseStepsException().withSubsequentSteps(revisedSteps);
        }

        if (model.isWillRollbackInvalid()) {
            throw new RuntimeException("Will not rollback");
        }

        if (model.isWillRollback()) {
            // 必须抛出FooException，如果抛出RuntimeException，会抛出: java.lang.ClassCastException: java.lang.RuntimeException cannot be cast to runtime.registry.mock.exception.FooException
            throw new FooException(rollbackReason);
        }
    }

    @Override
    public String stepCode() {
        return Steps.Submit.BarStep;
    }
}
