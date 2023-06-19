package io.github.dddplus.runtime.registry;

import io.github.dddplus.testing.AloneRunner;
import io.github.dddplus.testing.AloneWith;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(AloneRunner.class)
@AloneWith(JUnit4.class)
public class BadOnPurposeTest {

    private ClassPathXmlApplicationContext applicationContext;

    @After
    public void tearDown() {
        if (applicationContext != null) {
            applicationContext.close();
            applicationContext = null;
        }

        RegistryFactory.validRegistryEntries.clear();
        InternalIndexer.domainDefMap.clear();
        InternalIndexer.domainStepDefMap.clear();
        InternalIndexer.routerDefMap.clear();
        InternalIndexer.partnerDefMap.clear();
        InternalIndexer.patternDefMap.clear();
        InternalIndexer.policyDefMap.clear();
        InternalIndexer.extensionInterceptor = null;
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
    public void invalidPolicy() {
        try {
            applicationContext = new ClassPathXmlApplicationContext("policy-bad.xml");
            fail();
        } catch (BeanCreationException expected) {
            assertEquals("io.github.badcase.policy.InvalidPolicy MUST implements IPolicy", expected.getCause().getMessage());
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
    public void badRouter() {
        try {
            applicationContext = new ClassPathXmlApplicationContext("ability-bad.xml");
            fail();
        } catch (BeanCreationException expected) {
            assertEquals("io.github.badcase.router.bad1.BadRouter MUST extend BaseRouter", expected.getCause().getMessage());
        }
    }

    @Test
    public void badRouter2() {
        // StillLegalGenericRouter 虽然没有指明泛型的类型，它也是合法的
        applicationContext = new ClassPathXmlApplicationContext("ability-bad2.xml");
    }
}
