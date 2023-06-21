/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.core.importer.ImportOptions;
import com.tngtech.archunit.lang.ArchRule;
import io.github.dddplus.annotation.*;
import io.github.dddplus.ext.IDomainExtension;
import io.github.dddplus.ext.IIdentityResolver;
import io.github.dddplus.ext.IPolicy;
import io.github.dddplus.runtime.BasePattern;
import io.github.dddplus.runtime.BaseRouter;

import java.util.LinkedList;
import java.util.List;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

/**
 * DDDplus架构守护，based upon ArchUnit.
 * <p>
 * <ul>守护什么？
 * <li>架构分层关系</li>
 * <li>DDDplus框架使用规范，避免误用造成线上故障</li>
 * </ul>
 * <p>How to use?</p>
 * <p>In your CI unit test pipeline, add the following test case:</p>
 * <pre>
 * {@code
 *
 * new DDDPlusEnforcer()
 *         .scanPackages("your base packages")
 *         .enforce();
 * }
 * </pre>
 */
public class DDDPlusEnforcer {
    private List<ArchRule> rules = new LinkedList<>();
    private JavaClasses javaClasses;

    public DDDPlusEnforcer() {
        rules.add(IIdentityResolver());
        rules.add(IDomainExtensionImplementation());
        rules.add(IDomainExtensionDeclaration());
        rules.add(Router());
        rules.add(BaseRouter());
        rules.add(Pattern());
        rules.add(BasePattern());
        rules.add(IPolicy());
    }

    public DDDPlusEnforcer scanPackages(String... packages) {
        ImportOptions importOptions = new ImportOptions();
        importOptions.with(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS);
        javaClasses = new ClassFileImporter().importPackages(packages);
        return this;
    }

    public void enforce() {
        for (ArchRule rule : rules) {
            rule.check(javaClasses);
        }
    }

    /**
     * {@link BaseRouter}使用规范.
     * <p>
     * <p>类名后缀为Router，使用注解：{@link Router}</p >
     */
    private static final ArchRule BaseRouter() {
        return classes()
                .that().areAssignableTo(BaseRouter.class)
                .and().doNotHaveModifier(JavaModifier.ABSTRACT)
                .should().haveNameMatching(".*ExtRouter")
                .andShould().beAnnotatedWith(Router.class);
    }

    private static ArchRule Router() {
        return classes()
                .that().areAnnotatedWith(Router.class)
                .and().doNotHaveModifier(JavaModifier.ABSTRACT)
                .should().haveNameMatching(".*ExtRouter");
    }

    /**
     * {@link IIdentityResolver}使用规范.
     * <p>
     * <p>必须为：{@link Pattern} or {@link Partner}</p >
     */
    private static final ArchRule IIdentityResolver() {
        return classes()
                .that().areAssignableTo(IIdentityResolver.class)
                .and().doNotHaveModifier(JavaModifier.ABSTRACT)
                .should().beAnnotatedWith(Partner.class)
                .orShould().beAnnotatedWith(Pattern.class);
    }

    /**
     * {@link Pattern}使用规范.
     * <p>
     * <p>必须实现{@link IIdentityResolver}，类名后缀为"Pattern"</p >
     */
    private static final ArchRule Pattern() {
        class PatternAsResolver extends DescribedPredicate<JavaClass> {
            PatternAsResolver() {
                super("");
            }

            @Override
            public boolean apply(JavaClass javaClass) {
                Pattern pattern = javaClass.getAnnotationOfType(Pattern.class);
                // 非resolver类pattern排除在外，例如：pattern特有的application service
                return pattern.asResolver();
            }
        }

        return classes()
                .that().areAnnotatedWith(Pattern.class)
                .and().doNotHaveModifier(JavaModifier.ABSTRACT)
                .and(new PatternAsResolver())
                .should().beAssignableTo(IIdentityResolver.class)
                .andShould().haveNameMatching(".*Pattern");
    }

    /**
     * {@link BasePattern}使用规范.
     * <p>
     * <p>必须标注{@link Pattern}，类名后缀为"Pattern"</p >
     */
    private static final ArchRule BasePattern() {
        return classes()
                .that().areAssignableTo(BasePattern.class)
                .and().doNotHaveModifier(JavaModifier.ABSTRACT)
                .should().haveNameMatching(".*Pattern")
                .andShould().beAnnotatedWith(Pattern.class);
    }

    /**
     * {@link IPolicy}使用规范.
     * <p>
     * <p>必须标注{@link Policy}，类名后缀为"Policy"</p >
     */
    private static final ArchRule IPolicy() {
        return classes()
                .that().areAssignableTo(IPolicy.class)
                .and().doNotHaveModifier(JavaModifier.ABSTRACT)
                .should().haveNameMatching(".*ExtPolicy")
                .andShould().beAnnotatedWith(Policy.class);
    }

    /**
     * {@link IDomainExtension}实现类使用规范.
     * <p>
     * <p>类名后缀为"Ext"，使用注解{@link Extension}</p >
     */
    private static final ArchRule IDomainExtensionImplementation() {
        return classes()
                .that().areAssignableTo(IDomainExtension.class)
                .and().areNotInterfaces()
                .and().doNotHaveModifier(JavaModifier.ABSTRACT)
                .and().haveNameNotMatching(".Default*") // 默认的扩展点实现可以不使用 @Extension
                .should().haveNameMatching(".*Ext.*")
                .andShould().beAnnotatedWith(Extension.class);
    }

    /**
     * {@link IDomainExtension}定义类规范.
     * <p>
     * <p>类名为"IxxxExt"
     */
    private static final ArchRule IDomainExtensionDeclaration() {
        return classes()
                .that().areAssignableTo(IDomainExtension.class)
                .and().areInterfaces()
                .should().haveSimpleNameStartingWith("I")
                .andShould().haveSimpleNameEndingWith("Ext");
    }

}

