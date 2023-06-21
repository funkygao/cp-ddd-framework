/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus;

import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.lang.ArchRule;
import io.github.dddplus.annotation.*;
import io.github.dddplus.ext.IIdentityResolver;
import io.github.dddplus.model.IDomainModel;
import io.github.dddplus.model.IDomainService;
import io.github.dddplus.runtime.BaseRouter;
import io.github.dddplus.step.IDomainStep;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.lang.annotation.Target;
import java.util.LinkedList;
import java.util.List;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

/**
 * 架构守护神规则库，based upon ArchUnit.
 * <p>
 * <p>DDDplus框架的架构守护神，为架构演进保驾护航，拒绝架构腐化.</p>
 * <p>同时，也为本框架提供了静态检查机制(配合单元测试使用)，杜绝线上出现不合规范的使用.</p>
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
@Deprecated
public class ArchitectureEnforcer {

    private ArchitectureEnforcer() {
    }

    /**
     * 业务中台架构的所有规则.
     */
    public static final List<ArchRule> requiredRules = new LinkedList<>();

    public static final ArchRule serviceRule() {
        return classes()
                .that().haveNameMatching(".*Service")
                .should().implement(IDomainService.class)
                .andShould().beAnnotatedWith(DomainService.class)
                .as("Service必须是domain service，不能用于其他场景");
    }

    public static final ArchRule partnerDependencyRule() {
        return noClasses()
                .that().resideInAPackage("..bp..")
                .should().dependOnClassesThat().resideInAPackage("..domain..")
                .as("前台垂直业务包不能依赖domain包，必须依赖spec包");
    }

    public static final ArchRule routerRule() {
        return classes()
                .that().haveNameMatching(".*Router")
                .should().beAssignableTo(BaseRouter.class)
                .andShould().beAnnotatedWith(Router.class)
                .as("router必须继承BaseRouter，并且加@Router");
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

    /**
     * 所有接口名称必须以'I'开头，除了Dao/Manager以及对外的Api.
     */
    public static final ArchRule optionalInterfaceNameStartsWithI() {
        return classes()
                .that().areInterfaces()
                .and().areNotAnnotatedWith(Target.class) // 把注解本身排除掉
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

    public static final ArchRule optionalDddLayerRule() {
        return layeredArchitecture()
                .optionalLayer("Application").definedBy("..app..")
                .optionalLayer("Domain").definedBy("..domain..")
                .optionalLayer("Infrastructure").definedBy("..infra")
                .whereLayer("Application").mayNotBeAccessedByAnyLayer()
                .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Infrastructure")
                .whereLayer("Infrastructure").mayOnlyBeAccessedByLayers("Application")
                .as("DDD分层架构规范");
    }

    static {
        // 不允许使用System.out/System.in/System.err，printStackTrace
        requiredRules.add(NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS);
        // 不允许使用java.util.logging
        requiredRules.add(NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING);

        requiredRules.add(loggers_should_be_private_static_final());

        // DDD框架的使用规范
        requiredRules.add(repositoryRule());

        requiredRules.add(partnerDependencyRule());
        requiredRules.add(domainModelRule());
        requiredRules.add(serviceRule());
        requiredRules.add(noActivityClassAllowed());
        requiredRules.add(activityRule());
        requiredRules.add(aclRule());
        requiredRules.add(patternRule());
        requiredRules.add(routerRule());
        requiredRules.add(partnerRule());
        requiredRules.add(domainStepRule());

        requiredRules.add(controllers_should_only_use_their_own_slice());
    }
}
