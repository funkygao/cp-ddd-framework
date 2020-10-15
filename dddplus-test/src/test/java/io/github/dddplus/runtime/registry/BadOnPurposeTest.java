package io.github.dddplus.runtime.registry;

import io.github.dddplus.runtime.test.AloneRunner;
import io.github.dddplus.runtime.test.AloneWith;
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
            applicationContext.destroy();
            applicationContext = null;
        }

        InternalIndexer.domainDefMap.clear();
        InternalIndexer.domainStepDefMap.clear();
        InternalIndexer.domainAbilityDefMap.clear();
        InternalIndexer.partnerDefMap.clear();
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

}
