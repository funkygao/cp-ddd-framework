package org.x.cp.ddd.runtime;

import org.x.cp.ddd.annotation.Partner;
import org.x.cp.ddd.model.BaseDomainAbility;
import org.x.cp.ddd.model.IDomainStep;
import org.x.cp.ddd.runtime.registry.StepDef;
import org.x.cp.ddd.runtime.registry.InternalIndexer;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DDD框架的核心类.
 */
public final class DDD {
    private DDD() {
    }

    /**
     * 紧急下线某一个业务前台.
     * <p>
     * <p>典型场景：A/B两个业务前台在中台里部署在一个容器，A在线上出现严重错误，可能会影响B，这时候可以紧急下线A</p>
     * <p>紧急下线后，A的所有扩展点会强制抛出{@code PartnerKilledException}</p>
     *
     * @param code  {@link Partner#code()}
     * @param token 验证权限的token，以防止业务前台恶意调用
     */
    public static void killPartner(String code, String token) {
        InternalIndexer.killPartner(code);
    }

    /**
     * 恢复某一个被紧急下线的业务前台.
     *
     * @param code  {@link Partner#code()}
     * @param token 验证权限的token，以防止业务前台恶意调用
     */
    public static void recoverPartner(String code, String token) {
        InternalIndexer.recoverPartner(code);
    }

    /**
     * 定位一个领域能力点实例.
     *
     * @param domainAbilityClazz 领域能力类型
     * @param <T>                领域能力类型
     * @return null if bug found：研发忘记使用注解DomainAbility了
     */
    @NotNull
    public static <T extends BaseDomainAbility> T findAbility(@NotNull Class<? extends T> domainAbilityClazz) {
        return InternalIndexer.findDomainAbility(domainAbilityClazz);
    }

    /**
     * 根据步骤编号定位领域活动步骤.
     *
     * @param activityCode 所属领域活动编号
     * @param stepCodeList 领域步骤编号列表
     * @param <Step>       领域步骤类型
     * @return 如果没找到，会返回数量为0的非空列表
     */
    @NotNull
    public static <Step extends IDomainStep> List<Step> findSteps(@NotNull String activityCode, @NotNull List<String> stepCodeList) {
        List<StepDef> stepDefs = InternalIndexer.findDomainSteps(activityCode, stepCodeList);
        List<Step> result = new ArrayList<>(stepDefs.size());
        for (StepDef stepDef : stepDefs) {
            result.add((Step) stepDef.getStepBean());
        }

        return result;
    }

    /**
     * 定位某一个领域步骤实例.
     *
     * @param activityCode 所属领域活动编号
     * @param stepCode     步骤编号
     * @param <Step>       领域步骤类型
     * @return 如果找不到，则返回null
     */
    public static <Step extends IDomainStep> Step getStep(@NotNull String activityCode, @NotNull String stepCode) {
        List<Step> steps = findSteps(activityCode, Arrays.asList(stepCode));
        if (steps.size() == 1) {
            return steps.get(0);
        }

        return null;
    }

}
