package io.github.dddplus;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.core.importer.ImportOptions;
import com.tngtech.archunit.lang.ArchRule;
import io.github.dddplus.runtime.registry.mock.router.IllegalRouter;
import io.github.dddplus.runtime.registry.mock.extension.EggExt;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import org.junit.Before;
import org.junit.Test;

import static com.tngtech.archunit.base.DescribedPredicate.not;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.belongToAnyOf;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ArchitectureEnforcerTest {

    private JavaClasses classes;

    @Before
    public void setUp() {
        ImportOptions importOptions = new ImportOptions();
        importOptions.with(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS);
        classes = new ClassFileImporter().importPackages("io.github.dddplus.runtime.registry.mock");
        classes = classes.that(not(belongToAnyOf(
                // 这些类是故意写错的，要排除掉
                EggExt.class,
                IllegalRouter.class,

                FooModel.class // 本来domain model是不能直接new的，需要通过creator创建，但单元测试为了方便，允许直接new了
        )));
    }

    @Test
    public void requiredRules() {
        for (ArchRule rule : ArchitectureEnforcer.requiredRules) {
            rule.check(classes);
        }
    }

    @Test
    public void optionalDddLayerRule() {
        ArchitectureEnforcer.optionalDddLayerRule().check(classes);
    }

    @Test
    public void optionalInterfaceNameStartsWithI() {
        try {
            ArchitectureEnforcer.optionalInterfaceNameStartsWithI().check(classes);
            fail();
        } catch (AssertionError expected) {
            assertTrue(expected.getMessage().contains("Architecture Violation [Priority: MEDIUM] - Rule '接口名称必须以I开头' was violated"));
            assertTrue(expected.getMessage().contains(" violated (5 times)"));
            assertTrue(expected.getMessage().contains("simple name of io.github.dddplus.runtime.registry.mock.router.RouterTag does not start with 'I'"));
            assertTrue(expected.getMessage().contains("simple name of io.github.dddplus.runtime.registry.mock.pattern.Patterns does not start with 'I'"));
            assertTrue(expected.getMessage().contains("simple name of io.github.dddplus.runtime.registry.mock.step.Steps does not start with 'I'"));
            assertTrue(expected.getMessage().contains("simple name of io.github.dddplus.runtime.registry.mock.step.Steps$Cancel does not start with 'I'"));
            assertTrue(expected.getMessage().contains("simple name of io.github.dddplus.runtime.registry.mock.step.Steps$Submit does not start with 'I'"));
        }
    }

}