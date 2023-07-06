/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.runtime;

import io.github.dddplus.model.IDomainModel;
import io.github.dddplus.step.IDomainStep;
import io.github.dddplus.step.IReviseStepsException;
import io.github.dddplus.step.IRevokableDomainStep;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.ResolvableType;
import org.springframework.scheduling.SchedulingTaskExecutor;

import java.util.*;
import java.util.concurrent.RejectedExecutionException;

/**
 * 步骤编排的模板方法类.
 *
 * @param <Step>  领域步骤
 * @param <Model> 领域模型
 */
@Slf4j
@Deprecated
public abstract class StepsExecTemplate<Step extends IDomainStep, Model extends IDomainModel> {
    private static final List<String> emptyRevisedSteps = Collections.emptyList();
    private static final Set<String> emptyAsyncSteps = Collections.emptySet();

    private static final int MAX_STEP_REVISIONS = 100;

    protected void beforeStep(Step step, Model model) {
    }

    protected void afterStep(Step step, Model model) {
    }

    /**
     * 同步执行编排好的步骤.
     * <p>
     * <p>步骤的实现里，可以通过{@link IReviseStepsException}来进行后续步骤修订，即动态的步骤编排</p>
     * <p>如果步骤实现了{@link IRevokableDomainStep}，在步骤抛出异常后会自动触发步骤回滚</p>
     *
     * @param activityCode 领域活动
     * @param stepCodes    待执行的的领域步骤
     * @param model        领域模型
     * @throws RuntimeException 步骤执行时抛出的异常，统一封装为 RuntimeException
     */
    public final void execute(String activityCode, List<String> stepCodes, Model model) throws RuntimeException {
        execute(activityCode, stepCodes, model, null, emptyAsyncSteps);
    }

    /**
     * 执行编排好的步骤，支持异步执行指定的步骤.
     * <p>
     * <p>步骤的实现里，可以通过{@link IReviseStepsException}来进行后续步骤修订，即动态的步骤编排</p>
     * <p>如果步骤实现了{@link IRevokableDomainStep}，在步骤抛出异常后会自动触发步骤回滚</p>
     * <p>异步执行的步骤，注意事项：</p>
     * <ul>
     * <li>需要使用者保证线程安全性!</li>
     * <li>beforeStep/afterStep的执行，都是同步的，都在主线程内执行</li>
     * <li>异步执行的步骤的异常都被忽略，不会触发回滚</li>
     * <li>不支持在异步执行的步骤里修订后续步骤</li>
     * </ul>
     * <p>In all, async steps executes in fire and forget mode!</p>
     *
     * @param activityCode   领域活动
     * @param stepCodes      待执行的的领域步骤
     * @param model          领域模型
     * @param taskExecutor   异步执行的线程池容器
     * @param asyncStepCodes 异步执行的步骤. Attention: 异步执行的任务，在失败时是不会触发回滚的
     * @throws RuntimeException 步骤执行时抛出的异常，统一封装为 RuntimeException
     */
    public final void execute(String activityCode, List<String> stepCodes, Model model,
                              SchedulingTaskExecutor taskExecutor, Set<String> asyncStepCodes) throws RuntimeException {
        if (stepCodes == null || stepCodes.isEmpty()) {
            log.warn("Empty steps of activity:{} on {}", activityCode, model);
            return;
        }

        Stack<IRevokableDomainStep> executedSteps = new Stack<>();
        int stepRevisions = 0;
        while (++stepRevisions < MAX_STEP_REVISIONS) {
            // 执行步骤的过程中，可能会产生修订步骤逻辑
            stepCodes = executeSteps(activityCode, stepCodes, executedSteps, model, taskExecutor, asyncStepCodes);
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
    private List<String> executeSteps(String activityCode, List<String> stepCodes, Stack<IRevokableDomainStep> executedSteps, Model model,
                                      SchedulingTaskExecutor taskExecutor, Set<String> asyncStepCodes) throws RuntimeException {
        if (asyncStepCodes == null || taskExecutor == null) {
            // the sentry
            asyncStepCodes = emptyAsyncSteps;
        }

        List<Step> steps = DDD.findSteps(activityCode, stepCodes);
        String currentStepCode = null;
        try {
            for (Step step : steps) {
                currentStepCode = step.stepCode();

                // async step下，before/after step，都还在主线程内执行：否则用户无法完成 ThreadLocal 的切换机制
                beforeStep(step, model);

                if (asyncStepCodes.contains(currentStepCode)) {
                    // for async steps, fire and forget!
                    asyncExecuteStep(taskExecutor, step, model);
                } else {
                    step.execute(model);
                }

                afterStep(step, model);

                if (step instanceof IRevokableDomainStep && !asyncStepCodes.contains(currentStepCode)) {
                    // prepare for possible sync step rollback
                    // 异步执行的任务，在失败时是不会触发回滚的
                    executedSteps.push((IRevokableDomainStep) step);
                }
            }
        } catch (Exception cause) {
            if (cause instanceof IReviseStepsException) {
                // 重新编排(修订)了后续步骤
                // 仍在运行的async steps会继续执行，不必回收
                return ((IReviseStepsException) cause).subsequentSteps();
            }

            log.error("Step:{}.{} fails for {}", activityCode, currentStepCode, stepCodes, cause);

            if (cause instanceof RejectedExecutionException) {
                // taskExecutor thread pool full!
                throw (RejectedExecutionException) cause;
            }

            // 其他异常，best effort rollback if necessary
            if (!executedSteps.empty() && cause instanceof RuntimeException) {
                if (cause.getClass() == resolveStepExType()) { // Step必定是同一个ClassLoader加载的：中台统一加载
                    // 如果是Step的泛型里定义的异常，则回滚：回滚都是同步的
                    // 其他异常，不是业务显式抛出的，状态下确定，框架不敢擅自回滚：只能向上抛出，交由使用者处理
                    safeRollbackExecutedSteps(model, (RuntimeException) cause, executedSteps);
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

    private void asyncExecuteStep(SchedulingTaskExecutor taskExecutor, Step step, Model model) {
        Map<String, String> mdcContext = MDC.getCopyOfContextMap();
        taskExecutor.execute(() -> {
            // 切换到线程池，ThreadLocal会失效，目前ThreadLocal只有MDC
            // 如果业务系统有自己的ThreadLocal，可以通过 beforeStep/afterStep 机制进行处理
            MDC.setContextMap(mdcContext);
            try {
                step.execute(model); // IMPORTANT: model must be thread safe!
            } finally {
                MDC.clear();
            }
        });
    }

    private Class resolveStepExType() {
        Class thisClass;
        if (AopUtils.isAopProxy(this)) {
            thisClass = AopUtils.getTargetClass(this);
        } else {
            thisClass = this.getClass();
        }
        ResolvableType stepsExecType = ResolvableType.forClass(thisClass);
        ResolvableType templateType = stepsExecType.getSuperType();
        // 处理StepsExecTemplate的多层继承
        // 目前不够严谨，它假定了实现了IDomainStep的中间类不能再定义泛型
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

    private void safeRollbackExecutedSteps(Model model, RuntimeException cause, Stack<IRevokableDomainStep> executedSteps) {
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
