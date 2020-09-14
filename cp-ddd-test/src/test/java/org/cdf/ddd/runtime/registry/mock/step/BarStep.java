package org.cdf.ddd.runtime.registry.mock.step;

import lombok.extern.slf4j.Slf4j;
import org.cdf.ddd.annotation.Step;
import org.cdf.ddd.runtime.DDD;
import org.cdf.ddd.runtime.registry.mock.exception.FooDecideStepsException;
import org.cdf.ddd.runtime.registry.mock.exception.FooException;
import org.cdf.ddd.runtime.registry.mock.ability.ReviseStepsAbility;
import org.cdf.ddd.runtime.registry.mock.model.FooModel;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Step(groups = Steps.Submit.GoodsValidationGroup, dependsOn = FooStep.class)
@Slf4j
public class BarStep extends SubmitStep {
    public static final String rollbackReason = "rollback on purpose";

    @Override
    public void execute(@NotNull FooModel model) throws FooException {
        log.info("submit: {}", model);

        if (model.isRedecideDeadLoop()) {
            // 故意制造step死循环
            log.info("dead loop on purpose for step:{}", this.stepCode());
            List<String> revisedSteps = new ArrayList<>();
            revisedSteps.add(this.stepCode());
            throw new FooDecideStepsException().withSubsequentSteps(revisedSteps);
        }

        List<String> revisedSteps = DDD.findAbility(ReviseStepsAbility.class).revisedSteps(model);
        if (revisedSteps != null && !revisedSteps.isEmpty()) {
            log.info("重新编排步骤，增加步骤：{}", revisedSteps);

            // 通过异常，来改变后续步骤
            throw new FooDecideStepsException().withSubsequentSteps(revisedSteps);
        }

        if (model.isWillRollback()) {
            // 必须抛出FooException，如果抛出RuntimeException，会抛出: java.lang.ClassCastException: java.lang.RuntimeException cannot be cast to org.cdf.ddd.runtime.registry.mock.exception.FooException
            throw new FooException(rollbackReason);
        }
    }

    @Override
    public String stepCode() {
        return Steps.Submit.BarStep;
    }
}
