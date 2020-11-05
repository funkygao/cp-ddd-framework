package io.github.dddplus.runtime.registry;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import io.github.dddplus.ArchitectureEnforcer;
import io.github.dddplus.testing.AloneRunner;
import io.github.dddplus.testing.AloneWith;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(AloneRunner.class)
@AloneWith(JUnit4.class)
public class BadOnPurposeTest {

    private ClassPathXmlApplicationContext applicationContext;

    @After
    public void tearDown() {
        if (applicationContext != null) {
            applicationContext.destroy();
            applicationContext = null;
        }

        InternalIndexer.domainDefMap.clear();
        InternalIndexer.domainStepDefMap.clear();
        InternalIndexer.domainAbilityDefMap.clear();
        InternalIndexer.partnerDefMap.clear();
        InternalIndexer.patternDefMap.clear();
    }

    @Test
    public void specificationNotAnnotated() {
        try {
            ArchitectureEnforcer.specificationRule().check(new ClassFileImporter().importPackages("io.github.badcase"));
            fail();
        } catch (AssertionError expected) {
            expected.printStackTrace();
            assertTrue(expected.getMessage().contains("Rule 'ISpecification rule' was violated"));
            assertTrue(expected.getMessage().contains("<io.github.badcase.specification.SpecificationWithoutAnnotation> is not annotated with @Specification"));
        }
    }

    @Test
    public void emptyCodeStep() {
        try {
            applicationContext = new ClassPathXmlApplicationContext("step-emptycode.xml");
            fail();
        } catch (BeanCreationException expected) {
            assertEquals("io.github.badcase.step.emptycode.EmptyCodeStep stepCode cannot be empty", expected.getCause().getMessage());
        }
    }

    @Test
    public void emptyActivityStep() {
        try {
            applicationContext = new ClassPathXmlApplicationContext("step-emptyactivity.xml");
            fail();
        } catch (BeanCreationException expected) {
            assertEquals("io.github.badcase.step.emptyactivity.EmptyActivityStep activityCode cannot be empty", expected.getCause().getMessage());
        }
    }

    @Test
    public void notStepButAnnotatedWithStep() {
        try {
            applicationContext = new ClassPathXmlApplicationContext("step-notstep.xml");
            fail();
        } catch (BeanCreationException expected) {
            assertEquals("io.github.badcase.step.notstep.NotStepButAnnotatedWithStep MUST implement IDomainStep", expected.getCause().getMessage());
        }
    }

    @Test
    public void notExtButAnnotatedWithExtension() {
        try {
            applicationContext = new ClassPathXmlApplicationContext("ext-notext.xml");
            fail();
        } catch (BeanCreationException expected) {
            assertEquals("io.github.badcase.ext.NotExtButAnnotatedWithExtension MUST implement IDomainExtension", expected.getCause().getMessage());
        }
    }

    @Test
    public void patternWithInvalidPriority() {
        try {
            applicationContext = new ClassPathXmlApplicationContext("pattern-bad.xml");
            fail();
        } catch (BeanCreationException expected) {
            assertEquals("Pattern.priority must be zero or positive", expected.getCause().getMessage());
        }
    }

    @Test
    public void invalidPartner() {
        try {
            applicationContext = new ClassPathXmlApplicationContext("partner-bad.xml");
            fail();
        } catch (BeanCreationException expected) {
            assertEquals("io.github.badcase.partner.InvalidPartner MUST implements IIdentityResolver", expected.getCause().getMessage());
        }
    }

    @Test
    public void badDomainService() {
        try {
            applicationContext = new ClassPathXmlApplicationContext("service-bad.xml");
            fail();
        } catch (BeanCreationException expected) {
            assertEquals("io.github.badcase.service.BadDomainService MUST implement IDomainService", expected.getCause().getMessage());
        }
    }

    @Test
    public void badDomainAbility() {
        try {
            applicationContext = new ClassPathXmlApplicationContext("ability-bad.xml");
            fail();
        } catch (BeanCreationException expected) {
            assertEquals("io.github.badcase.ability.bad1.BadAbility MUST extend BaseDomainAbility", expected.getCause().getMessage());
        }
    }

    @Test
    public void badDomainAbility2() {
        try {
            applicationContext = new ClassPathXmlApplicationContext("ability-bad2.xml");
            fail();
        } catch (BeanCreationException expected) {
            assertEquals("cannot find ParameterizedType for:io.github.badcase.ability.bad2.IllegalGenericAbility", expected.getCause().getMessage());
        }
    }

}
