package org.ddd.cp.ddd.runtime;

import org.ddd.cp.ddd.model.IDomainModel;
import org.ddd.cp.ddd.step.IDomainRevokableStep;
import org.ddd.cp.ddd.step.IDomainStep;
import org.ddd.cp.ddd.step.IDecideStepsException;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * 步骤执行的模板方法类.
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
     * <p>步骤的实现里，可以通过{@link IDecideStepsException}来进行后续步骤修订，即动态的步骤编排</p>
     * <p>如果步骤实现了{@link IDomainRevokableStep}，在步骤抛出异常后会自动触发步骤回滚</p>
     *
     * @param activityCode 领域活动
     * @param stepCodes    (初次)编排好的步骤
     * @param model        领域模型
     */
    public final void execute(String activityCode, List<String> stepCodes, Model model) {
        Stack<IDomainRevokableStep> executedSteps = new Stack<>();
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
            log.error("Steps revision seem to encounter dead loop, abort:{}", model);
        }
    }

    // return revised steps
    private List<String> executeSteps(String activityCode, List<String> stepCodes, Stack<IDomainRevokableStep> executedSteps, Model model) {
        List<Step> steps = DDD.findSteps(activityCode, stepCodes);
        try {
            for (Step step : steps) {
                beforeStep(step, model);
                step.execute(model);
                afterStep(step, model);

                if (step instanceof IDomainRevokableStep) {
                    // prepare for possible rollback
                    executedSteps.push((IDomainRevokableStep) step);
                }
            }
        } catch (Exception cause) {
            if (cause instanceof IDecideStepsException) {
                // 重新编排(修订)了后续步骤
                return ((IDecideStepsException) cause).subsequentSteps();
            }

            // 其他异常，best effort rollback后直接抛出
            if (cause instanceof RuntimeException) {
                rollbackExecutedSteps(model, (RuntimeException) cause, executedSteps);
            }

            throw cause;
        }

        return emptyRevisedSteps;
    }

    private void rollbackExecutedSteps(Model model, RuntimeException cause, Stack<IDomainRevokableStep> executedSteps) {
        while (!executedSteps.isEmpty()) {
            // 失败时，按照反方向执行回滚操作：Saga Pattern
            IDomainRevokableStep executedStep = executedSteps.pop();
            try {
                executedStep.rollback(model, cause);
            } catch (Throwable ignored) {
                log.error("{} step:{} rollback err ignored", model, executedStep.stepCode(), ignored);
            }
        }
    }

}
