package io.github.dddplus.runtime.registry;

import io.github.dddplus.testing.AloneRunner;
import io.github.dddplus.testing.AloneWith;
import io.github.dddplus.testing.LogAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

@RunWith(AloneRunner.class)
@AloneWith(JUnit4.class)
public class DDDBootstrapTest {

    @Test
    public void reload() throws IOException {
        final String expectedLogEvent = "register applicationContext more than once, ignored!";
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-test.xml");
        assertNotNull(DDDBootstrap.applicationContext());
        assertSame(applicationContext, DDDBootstrap.applicationContext());
        LogAssert.assertNotContains(expectedLogEvent, "Spring reloaded complete!");

        DDDBootstrap dddBootstrap = applicationContext.getBean(DDDBootstrap.class);

        dddBootstrap.setApplicationContext(applicationContext);
        LogAssert.assertContains(expectedLogEvent);

        GenericApplicationContext genericApplicationContext = new GenericApplicationContext();
        genericApplicationContext.setParent(applicationContext);
        ContextRefreshedEvent contextRefreshedEvent = new ContextRefreshedEvent(genericApplicationContext);
        dddBootstrap.onApplicationEvent(contextRefreshedEvent);
        LogAssert.assertContains("Spring reloaded complete!");

        applicationContext.close();
    }

}