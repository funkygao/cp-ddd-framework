package org.ddd.cp.ddd.runtime.registry;

import org.ddd.cp.ddd.annotation.Partner;
import org.ddd.cp.ddd.ext.IDomainExtension;
import org.ddd.cp.ddd.model.IDomainModel;
import org.ddd.cp.ddd.model.BaseDomainAbility;
import org.ddd.cp.ddd.runtime.DDD;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * 注册表索引, internal usage only.
 */
@Slf4j
public class InternalIndexer {
    private InternalIndexer() {
    }

    private static final List<StepDef> emptySteps = Collections.emptyList();

    static final Map<String, DomainDef> domainDefMap = new HashMap<>(); // {code, def}
    static final Map<Class<? extends BaseDomainAbility>, DomainAbilityDef> domainAbilityDefMap = new HashMap<>();
    static final Map<String, Map<String, StepDef>> domainStepDefMap = new HashMap<>(); // {activityCode, {stepCode, def}}

    // 扩展点
    static final Map<String, PatternDef> patternDefMap = new HashMap<>();
    static final Map<Class<? extends IDomainExtension>, List<PatternDef>> sortedPatternMap = new HashMap<>();
    static final Map<String, PartnerDef> partnerDefMap = new HashMap<>();

    private static String killedPartnerCode;

    /**
     * 把一个{@link Partner}紧急下线, internal usage only.
     *
     * @param code {@link Partner#code()}
     */
    public static void killPartner(String code) {
        Class caller = org.slf4j.helpers.Util.getCallingClass();
        if (!caller.equals(DDD.class)) {
            // 只允许DDD里调用，否则被恶意前台调用就毁了
            throw new RuntimeException("killPartner caller can only be DDD! Bye hacker!");
        }

        killedPartnerCode = code;
    }

    public static void recoverPartner(String code) {
        Class caller = org.slf4j.helpers.Util.getCallingClass();
        if (!caller.equals(DDD.class)) {
            throw new RuntimeException("recoverPartner caller can only be DDD! Bye hacker!");
        }

        killedPartnerCode = null;
    }

    /**
     * 根据业务能力类找到一个业务能力实例, internal usage only.
     *
     * @param clazz 业务能力类
     * @param <T>
     * @return 业务能力实例, null if not found
     */
    public static <T extends BaseDomainAbility> T findDomainAbility(@NotNull Class<? extends T> clazz) {
        DomainAbilityDef domainAbilityDef = domainAbilityDefMap.get(clazz);
        if (domainAbilityDef == null) {
            // 研发忘记使用注解DomainAbility了，线上bug
            // 但如果没有关闭架构守护神ArchitectureEnforcer，则不可能出现该bug
            log.error("{} forgot to apply @DomainAbility, ArchitectureEnforcer turned off? indexed:{}", clazz.getCanonicalName(), domainAbilityDefMap.keySet());
            return null;
        }

        return (T) domainAbilityDef.getDomainAbilityBean();
    }

    /**
     * 给定一个领域能力，找到它定义的扩展点接口, internal usage only.
     *
     * @param clazz
     * @return
     */
    public static Class<? extends IDomainExtension> getDomainAbilityExtDeclaration(@NotNull Class<? extends BaseDomainAbility> clazz) {
        DomainAbilityDef domainAbilityDef = domainAbilityDefMap.get(clazz);
        if (domainAbilityDef == null) {
            // 研发忘记使用注解DomainAbility了，线上bug
            log.error("{} not apply @DomainAbility", clazz.getCanonicalName());
            return null;
        }

        return domainAbilityDef.getExtClazz();
    }

    /**
     * 获取某一个扩展点的所有实现实例.
     *
     * @param extClazz  extension interface
     * @param model     domain model
     * @param firstStop 是否找到一个就返回
     * @return 有效的扩展点列表, empty List if not found
     */
    @NotNull
    public static List<ExtensionDef> findEffectiveExtensions(@NotNull Class<? extends IDomainExtension> extClazz, @NotNull IDomainModel model, boolean firstStop) {
        List<ExtensionDef> effectiveExtensions = new LinkedList<>();

        // Pattern优先：细粒度的扩展点
        // 否则，Pattern下的扩展点可能会被粗粒度的Partner下扩展点给盖住，无法执行
        // Partner=KA，在Partner下它实现了一个扩展点，这时候签约了一个新KA客户(宝胜)的逻辑稍有不同，因此实现了一个Pattern下的扩展点
        // 如果Partner优先，那么宝胜的扩展点实现永远不会被执行：它被Partner下的扩展点盖住了 -:(
        // 同时，Pattern是有优先级的，典型场景：一个Seller有一个Pattern，该Seller下有2个特殊的Dept，各自有自己的Pattern
        List<PatternDef> sortedPatternDefs = sortedPatternMap.get(extClazz);
        if (sortedPatternDefs != null && !sortedPatternDefs.isEmpty()) {
            // 该扩展点在一些Pattern上有实现，那么通过Pattern把扩展点实例找到
            log.debug("{} found patterns:{}", extClazz.getCanonicalName(), sortedPatternDefs);

            for (PatternDef patternDef : sortedPatternDefs) {
                if (!patternDef.match(model)) {
                    continue;
                }

                // 找该Pattern下实现了该扩展点接口的实例
                ExtensionDef extensionDef = patternDef.getExtension(extClazz);
                if (extensionDef != null) {
                    effectiveExtensions.add(extensionDef);
                }

                if (firstStop && !effectiveExtensions.isEmpty()) {
                    return effectiveExtensions;
                }
            }
        }

        // 之后再找Partner
        for (PartnerDef partnerDef : partnerDefMap.values()) {
            if (!partnerDef.match(model)) {
                continue;
            }

            if (partnerDef.getCode().equals(killedPartnerCode)) {
                throw new PartnerKilledException(partnerDef.getCode());
            }

            ExtensionDef extensionDef = partnerDef.getExtension(extClazz);
            if (extensionDef != null) {
                effectiveExtensions.add(extensionDef);
                break; // 垂直业务是互斥的，不可叠加的
            }
        }

        return effectiveExtensions;
    }

    /**
     * 根据指定条件获取匹配的领域活动步骤列表.
     *
     * @param activityCode 领域活动码，即该步骤属于哪一个领域活动
     * @param stepCodeList 活动步骤的编号{@code code}列表
     * @return 匹配的活动步骤列表, will never be null
     */
    @NotNull
    public static List<StepDef> findDomainSteps(@NotNull String activityCode, @NotNull List<String> stepCodeList) {
        Map<String, StepDef> childMap = domainStepDefMap.get(activityCode);
        if (childMap == null || childMap.isEmpty()) {
            log.error("found NO activity:{}", activityCode);
            return emptySteps;
        }

        List<StepDef> result = new LinkedList<>();
        for (String code : stepCodeList) {
            StepDef stepDef = childMap.get(code);
            if (stepDef != null) {
                result.add(stepDef);
            }
        }

        if (result.size() != stepCodeList.size()) {
            log.warn("expected:{}, got:{} domain steps", stepCodeList.size(), result.size());
        }

        return result;
    }

    static void indexDomainStep(StepDef stepDef) {
        if (!domainStepDefMap.containsKey(stepDef.getActivity())) {
            domainStepDefMap.put(stepDef.getActivity(), new HashMap<>());
        }

        Map<String, StepDef> childMap = domainStepDefMap.get(stepDef.getActivity());
        if (childMap.containsKey(stepDef.getCode())) {
            throw BootstrapException.ofMessage("duplicated step code: ", stepDef.getCode());
        }

        childMap.put(stepDef.getCode(), stepDef);
        log.info("indexed {} ", stepDef);
    }

    static void indexDomain(DomainDef domainDef) {
        if (domainDefMap.containsKey(domainDef.getCode())) {
            throw BootstrapException.ofMessage("duplicated domain code: ", domainDef.getCode());
        }

        domainDefMap.put(domainDef.getCode(), domainDef);
        log.info("indexed {}", domainDef);
    }

    static void indexDomainAbility(DomainAbilityDef domainAbilityDef) {
        if (!domainDefMap.containsKey(domainAbilityDef.getDomain())) {
            throw BootstrapException.ofMessage("activity domain not found: ", domainAbilityDef.getDomain());
        }

        if (domainAbilityDefMap.containsKey(domainAbilityDef.getDomainAbilityClass())) {
            throw BootstrapException.ofMessage("duplicated domain ability: ", domainAbilityDef.getDomainAbilityBean().toString());
        }

        domainAbilityDefMap.put(domainAbilityDef.getDomainAbilityClass(), domainAbilityDef);
        log.info("indexed {}", domainAbilityDef);
    }

    static void indexDomainService(DomainServiceDef domainServiceDef) {
        if (!domainDefMap.containsKey(domainServiceDef.getDomain())) {
            throw BootstrapException.ofMessage("activity domain not found: ", domainServiceDef.getDomain());
        }
        log.info("indexed {}", domainServiceDef);
    }

    static void indexExtension(ExtensionDef extensionDef) {
        if (patternDefMap.containsKey(extensionDef.getCode())) {
            // 基于Pattern的扩展点，因为在pattern里找到了对应的code：extension.code = pattern.code
            PatternDef patternDef = patternDefMap.get(extensionDef.getCode());
            patternDef.registerExtensionDef(extensionDef);

            log.info("indexed {} on {}", extensionDef, patternDef);
            return;
        }

        if (partnerDefMap.containsKey(extensionDef.getCode())) {
            // 基于垂直业务的扩展点
            PartnerDef partnerDef = partnerDefMap.get(extensionDef.getCode());
            partnerDef.registerExtensionDef(extensionDef);

            log.info("indexed {} on {}", extensionDef, partnerDef);
            return;
        }

        if (!IDomainExtension.DefaultCode.equals(extensionDef.getCode())) {
            // 扩展点编码，不属于Pattern也不属于Partner，也不是默认的扩展点实现
            throw BootstrapException.ofMessage("invalid extension code: ", extensionDef.getCode());
        }
    }

    static void indexPattern(PatternDef patternDef) {
        if (patternDefMap.containsKey(patternDef.getCode())) {
            throw BootstrapException.ofMessage("duplicated pattern code: ", patternDef.getCode());
        }

        // pattern.code不能与project.code冲突
        if (partnerDefMap.containsKey(patternDef.getCode())) {
            throw BootstrapException.ofMessage("pattern: ", patternDef.getCode(), " conflicts with project code");
        }

        patternDefMap.put(patternDef.getCode(), patternDef);
        log.info("indexed {}", patternDef);
    }

    static void indexPartner(PartnerDef partnerDef) {
        if (partnerDefMap.containsKey(partnerDef.getCode())) {
            throw BootstrapException.ofMessage("duplicated partner code: ", partnerDef.getCode());
        }

        // pattern.code不能与project.code冲突
        if (patternDefMap.containsKey(partnerDef.getCode())) {
            throw BootstrapException.ofMessage("partner: ", partnerDef.getCode(), " conflicts with pattern code");
        }

        partnerDefMap.put(partnerDef.getCode(), partnerDef);
        log.info("indexed {}", partnerDef);
    }

    static void postIndexing() {
        for (PatternDef patternDef : patternDefMap.values()) {
            for (Class<? extends IDomainExtension> extClazz : patternDef.extClazzSet()) {
                if (!sortedPatternMap.containsKey(extClazz)) {
                    sortedPatternMap.put(extClazz, new ArrayList<>());
                }

                sortedPatternMap.get(extClazz).add(patternDef);
            }
        }

        for (List<PatternDef> patternDefs : sortedPatternMap.values()) {
            Collections.sort(patternDefs, (p1, p2) -> p1.getPriority() - p2.getPriority());
        }

        // patternDefMap在运行时已经没有用了
        patternDefMap.clear();
    }
}
