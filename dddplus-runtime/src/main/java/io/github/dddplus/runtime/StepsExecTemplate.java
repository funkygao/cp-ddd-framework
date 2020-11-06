/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.runtime;

import io.github.dddplus.model.IDomainModel;
import io.github.dddplus.step.IDomainStep;
import lombok.extern.slf4j.Slf4j;
import io.github.dddplus.step.IReviseStepsException;
import io.github.dddplus.step.IRevokableDomainStep;
import org.springframework.core.ResolvableType;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * 步骤编排的模板方法类.
 *
 * @param <Step>  领域步骤
 * @param <Model> 领域模型
 */
@Slf4j
public abstract class StepsExecTemplate<Step extends IDomainStep, Model extends IDomainModel> {
    private static final List<String> emptyRevisedSteps = Collections.emptyList();
    private static final int MAX_STEP_REVISIONS = 100;

    protected void beforeStep(Step step, Model model) {
    }

    protected void afterStep(Step step, Model model) {
    }

    /**
     * 执行编排好的步骤.
     * <p>
     * <p>步骤的实现里，可以通过{@link IReviseStepsException}来进行后续步骤修订，即动态的步骤编排</p>
     * <p>如果步骤实现了{@link IRevokableDomainStep}，在步骤抛出异常后会自动触发步骤回滚</p>
     *
     * @param activityCode 领域活动
     * @param stepCodes    (初次)编排好的步骤
     * @param model        领域模型
     */
    public final void execute(String activityCode, List<String> stepCodes, Model model) {
        if (stepCodes == null || stepCodes.isEmpty()) {
            log.warn("Empty steps of activity:{} on {}", activityCode, model);
            return;
        }

        Stack<IRevokableDomainStep> executedSteps = new Stack<>();
        int stepRevisions = 0;
        while (++stepRevisions < MAX_STEP_REVISIONS) {
            // 执行步骤的过程中，可能会产生修订步骤逻辑
            stepCodes = executeSteps(activityCode, stepCodes, executedSteps, model);
            if (stepCodes.isEmpty()) {
                // 不再有步骤修订了：所有步骤都执行完毕
                break;
            }

            // 修订了后续步骤，记录个日志，then next loop
            log.info("revised steps:{}", stepCodes);
        }

        if (stepRevisions == MAX_STEP_REVISIONS) {
            // e,g. (a -> b(revise) -> a)
            log.error("Steps revision seem to encounter dead loop, abort after {} model:{}", stepRevisions, model);
            throw new RuntimeException("Seems steps dead loop, abort after " + MAX_STEP_REVISIONS);
        }
    }

    // return revised steps
    private List<String> executeSteps(String activityCode, List<String> stepCodes, Stack<IRevokableDomainStep> executedSteps, Model model) {
        List<Step> steps = DDD.findSteps(activityCode, stepCodes);
        try {
            for (Step step : steps) {
                beforeStep(step, model);
                step.execute(model);
                afterStep(step, model);

                if (step instanceof IRevokableDomainStep) {
                    // prepare for possible rollback
                    executedSteps.push((IRevokableDomainStep) step);
                }
            }
        } catch (Exception cause) {
            if (cause instanceof IReviseStepsException) {
                // 重新编排(修订)了后续步骤
                return ((IReviseStepsException) cause).subsequentSteps();
            }

            // 其他异常，best effort rollback if necessary
            if (!executedSteps.empty() && cause instanceof RuntimeException) {
                if (cause.getClass() == getStepExType()) { // Step必定是同一个ClassLoader加载的：中台统一加载
                    // 如果是Step的泛型里定义的异常，则回滚
                    rollbackExecutedSteps(model, (RuntimeException) cause, executedSteps);
                } else {
                    // 其他类异常不回滚
                    log.debug("will not rollback, {} thrown", cause.getClass().getCanonicalName());
                }
            }

            // cause thrown as it is
            throw cause;
        }

        return emptyRevisedSteps;
    }

    private Class getStepExType() {
        ResolvableType stepsExecType = ResolvableType.forClass(this.getClass());
        ResolvableType templateType = stepsExecType.getSuperType();
        // 处理StepsExecTemplate的多层继承
        while (templateType.getGenerics().length == 0) {
            templateType = templateType.getSuperType();
        }

        // 找到了Step的泛型定义，然后找Step的Ex泛型的具体类型
        ResolvableType stepType = templateType.getGeneric(0);

        // Step实现多个接口的场景
        for (ResolvableType stepInterfaceType : stepType.getInterfaces()) {
            if (IDomainStep.class.isAssignableFrom(stepInterfaceType.resolve())) {
                return stepInterfaceType.getGeneric(1).resolve();
            }
        }

        // should never happen
        log.error("Cannot tell Step.Ex type for {}", this.getClass());
        return null;
    }

    private void rollbackExecutedSteps(Model model, RuntimeException cause, Stack<IRevokableDomainStep> executedSteps) {
        while (!executedSteps.isEmpty()) {
            // 失败时，按照反方向执行回滚操作：Sagas Pattern, best effort
            IRevokableDomainStep executedStep = executedSteps.pop();
            try {
                executedStep.rollback(model, cause);
            } catch (Throwable ignored) {
                log.error("step:{} rollback err ignored, model:{}", executedStep.stepCode(), model, ignored);
            }
        }
    }

}
