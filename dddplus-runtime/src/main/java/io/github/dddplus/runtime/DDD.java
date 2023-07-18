/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.runtime;

import io.github.dddplus.ext.IDomainExtension;
import io.github.dddplus.ext.IPolicy;
import io.github.dddplus.ext.IIdentity;
import io.github.dddplus.runtime.registry.InternalIndexer;
import io.github.dddplus.runtime.registry.StepDef;
import io.github.dddplus.step.IDomainStep;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DDD框架的核心类，定位核心对象.
 */
public final class DDD {
    private DDD() {
    }

    /**
     * 定位一个扩展点路由实例.
     *
     * @param routerClazz 扩展点路由器类型
     * @param <T>         扩展点路由器类型
     * @return null if bug found：研发忘记使用注解{@link io.github.dddplus.annotation.Router}了
     */
    @NonNull
    public static <T extends BaseRouter> T useRouter(@NonNull Class<? extends T> routerClazz) {
        return InternalIndexer.findRouter(routerClazz);
    }

    /**
     * 使用{@link IPolicy}路由策略定位扩展点实例.
     *
     * @param policy   扩展点路由策略类型
     * @param identity 业务身份，业务特征
     * @param <Ext>    扩展点
     * @return
     */
    public static <Ext extends IDomainExtension, Identity extends IIdentity> Ext usePolicy(@NonNull Class<? extends IPolicy<Ext, Identity>> policy, @NonNull Identity identity) {
        return firstExtension(InternalIndexer.extClazzOfPolicy(policy), identity);
    }

    /**
     * 绕过 {@link BaseRouter}，直接获取扩展点实例，通过{@link IPolicy}路由.
     * <p>
     * <p>有的控制点：</p>
     * <ul>
     * <li>不需要默认的扩展点实现</li>
     * <li>不会有复杂的 {@link IReducer} 逻辑，取到第一个匹配的即可</li>
     * <li>没有很强的业务属性：它可能是出于技术考虑而抽象出来的，而不是业务抽象</li>
     * </ul>
     * <p>这些场景下，{@link BaseRouter} 显得有些多此一举，可直接使用 {@link DDD#firstExtension(Class, IIdentity)}</p>
     * <p>但此时需要提供实现{@link IPolicy}</p>
     *
     * @param extClazz 扩展点类型
     * @param identity 业务身份，用于定位扩展点
     * @param <Ext>    扩展点
     */
    public static <Ext extends IDomainExtension> Ext firstExtension(@NonNull Class<Ext> extClazz, @NonNull IIdentity identity) {
        return firstExtension(extClazz, identity, 0);
    }

    /**
     * 绕过 {@link BaseRouter}，直接获取扩展点实例，通过{@link IPolicy}路由.
     * <p>
     * <p>有的控制点：</p>
     * <ul>
     * <li>不需要默认的扩展点实现</li>
     * <li>不会有复杂的 {@link IReducer} 逻辑，取到第一个匹配的即可</li>
     * <li>没有很强的业务属性：它可能是出于技术考虑而抽象出来的，而不是业务抽象</li>
     * </ul>
     * <p>这些场景下，{@link BaseRouter} 显得有些多此一举，可直接使用 {@link DDD#firstExtension(Class, IIdentity)}</p>
     * <p>但此时需要提供实现{@link IPolicy}</p>
     *
     * @param extClazz    扩展点类型
     * @param identity    业务身份，用于定位扩展点
     * @param timeoutInMs 执行扩展点的超时时间，in ms；如果超时，会强行终止扩展点的执行
     * @param <Ext>       扩展点
     * @param <R>         Reducer
     */
    private static <Ext extends IDomainExtension, R> Ext firstExtension(@NonNull Class<Ext> extClazz, @NonNull IIdentity identity, int timeoutInMs) {
        ExtensionInvocationHandler<Ext, R> proxy = new ExtensionInvocationHandler(extClazz, identity, null, null, InternalIndexer.registeredInterceptor(), timeoutInMs);
        return proxy.createProxy();
    }

    /**
     * 定位某一个领域步骤实例.
     *
     * @param activityCode 所属领域活动编号
     * @param stepCode     步骤编号
     * @param <Step>       领域步骤类型
     * @return 如果找不到，则返回null
     */
    @Deprecated
    public static <Step extends IDomainStep> Step getStep(@NonNull String activityCode, @NonNull String stepCode) {
        List<Step> steps = findSteps(activityCode, Arrays.asList(stepCode));
        if (steps.size() == 1) {
            return steps.get(0);
        }

        return null;
    }

    /**
     * 根据步骤编号定位领域活动步骤.
     *
     * @param activityCode 所属领域活动编号
     * @param stepCodeList 领域步骤编号列表
     * @param <Step>       领域步骤类型
     * @return 如果没找到，会返回数量为0的非空列表
     */
    @Deprecated
    public static <Step extends IDomainStep> List<Step> findSteps(@NonNull String activityCode, @NonNull List<String> stepCodeList) {
        List<StepDef> stepDefs = InternalIndexer.findDomainSteps(activityCode, stepCodeList);
        List<Step> result = new ArrayList<>(stepDefs.size());
        for (StepDef stepDef : stepDefs) {
            result.add((Step) stepDef.getStepBean());
        }

        return result;
    }

}
