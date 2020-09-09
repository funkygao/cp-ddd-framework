/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.ddd.cp.ddd;

import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.lang.ArchRule;
import org.ddd.cp.ddd.annotation.*;
import org.ddd.cp.ddd.ext.IDomainExtension;
import org.ddd.cp.ddd.ext.IIdentityResolver;
import org.ddd.cp.ddd.model.*;
import org.ddd.cp.ddd.step.IDomainStep;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

/**
 * 架构守护神规则库.
 * <p>
 * <p>业务中台DDD框架的架构守护神，为架构演进保驾护航，拒绝架构腐化.</p>
 * <p>https://www.archunit.org/motivation</p>
 * <p>
 * <pre>
 *  _      xxxx      _
 * /_;-.__ / _\  _.-;_\
 * `-._`'`_/'`.-'
 *     `\   /`
 *      |  /
 *     /-.(
 *     \_._\
 *      \ \`;
 *       > |/
 *      / //
 *      |//
 *      \(\
 *       ``
 * </pre>
 */
public class ArchitectureEnforcer {

    private ArchitectureEnforcer() {
    }

    /**
     * 业务中台架构的所有规则.
     */
    public static final List<ArchRule> allRules = new LinkedList<>();

    public static final ArchRule serviceRule() {
        return classes()
                .that().haveNameMatching(".*Service")
                .should().implement(IDomainService.class)
                .andShould().beAnnotatedWith(DomainService.class)
                .as("Service必须是domain service，不能用于其他场景");
    }

    public static final ArchRule projectDependencyRule() {
        return noClasses()
                .that().resideInAPackage("..bp..")
                .should().dependOnClassesThat().resideInAPackage("..domain..")
                .as("前台垂直业务包不能依赖domain包，必须依赖spec包");
    }

    public static final ArchRule abilityRule() {
        return classes()
                .that().haveNameMatching(".*Ability")
                .should().beAssignableTo(BaseDomainAbility.class)
                .andShould().beAnnotatedWith(DomainAbility.class)
                .as("ability必须继承BaseDomainAbility，并且加@DomainAbility");
    }

    public static final ArchRule partnerRule() {
        return classes()
                .that().haveNameMatching(".*Partner")
                .should().beAnnotatedWith(Partner.class)
                .as("Partner使用规范");
    }

    public static final ArchRule noActivityClassAllowed() {
        return noClasses()
                .should().haveNameMatching(".*Activity")
                .as("Activity必须定义成Step");
    }

    public static final ArchRule activityRule() {
        return classes()
                .that().haveSimpleNameEndingWith("Step")
                .and().haveModifier(JavaModifier.ABSTRACT)
                .should().implement(IDomainStep.class)
                .as("Activity使用规范");
    }

    public static final ArchRule domainStepRule() {
        return classes()
                .that().implement(IDomainStep.class)
                .and().doNotHaveModifier(JavaModifier.ABSTRACT) // 排除Activity
                .should().haveSimpleNameEndingWith("Step")
                .andShould().beAnnotatedWith(Step.class)
                .as("领域步骤的使用规范");
    }

    public static final ArchRule patternRule() {
        return classes()
                .that().haveNameMatching(".*Pattern")
                .and().doNotHaveModifier(JavaModifier.ABSTRACT)
                .should().beAssignableTo(IIdentityResolver.class)
                .andShould().beAnnotatedWith(Pattern.class)
                .as("Pattern的使用规范");
    }

    public static final ArchRule extensionRule() {
        return classes()
                .that().areAssignableTo(IDomainExtension.class)
                .and().areNotInterfaces()
                .and().haveNameNotMatching(".Default*") // 默认的扩展点实现可以不使用 @Extension
                .should().haveNameMatching(".*Ext")
                .andShould().beAnnotatedWith(Extension.class)
                .as("扩展点实现的规范");
    }

    public static final ArchRule loggers_should_be_private_static_final() {
        return fields()
                .that().haveRawType(Logger.class)
                .should().bePrivate()
                .andShould().beStatic()
                .andShould().beFinal()
                .because("by convention");
    }

    public static final ArchRule domainModelRule() {
        return classes()
                .that().implement(IDomainModel.class)
                .should().haveOnlyPrivateConstructors()
                .as("DomainModel不能直接new");
    }

    public static final ArchRule creatorRule() {
        return classes()
                .that().haveNameMatching(".*Creator")
                .should().implement(IDomainModelCreator.class)
                .as("Creator rule");
    }

    /**
     * 所有接口名称必须以'I'开头，除了Dao/Manager以及对外的Api.
     */
    public static final ArchRule interfaces_name_starts_with_i() {
        return classes()
                .that().areInterfaces()
                .and().haveNameNotMatching(".*Dao")
                .and().haveNameNotMatching(".*Manager")
                .and().haveNameNotMatching(".*Api")
                .and().haveNameNotMatching(".*Group") // 排除JSR303 group interface
                .should().haveSimpleNameStartingWith("I")
                .as("接口名称必须以I开头");
    }

    public static final ArchRule controllers_should_only_use_their_own_slice() {
        return slices()
                .matching("..controller.(*)..").namingSlices("Controller $1")
                .as("Controllers")
                .should().notDependOnEachOther();
    }

    public static final ArchRule repositoryRule() {
        return classes()
                .that().haveNameMatching(".*Repository")
                .and().areNotInterfaces()
                .should().beAnnotatedWith(Repository.class)
                .as("Repository必须用@Repository注解");
    }

    public static final ArchRule aclRule() {
        return classes()
                .that().haveNameMatching(".*Acl")
                .and().areNotInterfaces()
                .should().resideInAPackage("..acl..")
                .andShould().beAnnotatedWith(Component.class)
                .as("ACL类的使用规范");
    }

    public static final ArchRule dddLayerRule() {
        return layeredArchitecture()
                .optionalLayer("Application").definedBy("..app..")
                .optionalLayer("Domain").definedBy("..domain..")
                .optionalLayer("Infrastructure").definedBy("..infra")
                .whereLayer("Application").mayNotBeAccessedByAnyLayer()
                .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Infrastructure")
                .whereLayer("Infrastructure").mayOnlyBeAccessedByLayers("Application")
                .as("DDD分层架构规范");
    }

    public static final ArchRule dddArchitectureRule() {
        return onionArchitecture()
                .withOptionalLayers(true)
                .domainModels("..domain.model..")
                .domainServices("..domain.service..")
                .applicationServices("..app..")
                .as("dddArchitectureRule");
    }

    static {
        // 不允许使用System.out/System.in/System.err，printStackTrace
        allRules.add(NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS);
        // 不允许使用java.util.logging
        allRules.add(NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING);

        allRules.add(loggers_should_be_private_static_final());
        allRules.add(interfaces_name_starts_with_i());

        // DDD框架的使用规范
        allRules.add(repositoryRule());
        //allRules.add(dddArchitectureRule());
        //allRules.add(dddLayerRule());

        allRules.add(creatorRule());
        allRules.add(domainModelRule());
        allRules.add(serviceRule());
        allRules.add(noActivityClassAllowed());
        allRules.add(activityRule());
        allRules.add(aclRule());
        allRules.add(patternRule());
        allRules.add(abilityRule());
        allRules.add(partnerRule());
        allRules.add(domainStepRule());
        allRules.add(extensionRule());

        allRules.add(controllers_should_only_use_their_own_slice());
    }
}
