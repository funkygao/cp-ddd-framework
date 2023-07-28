package io.github.dddplus.runtime.registry;

import com.google.caliper.model.Run;
import io.github.dddplus.plugin.IContainerContext;
import io.github.dddplus.runtime.registry.mock.router.BarRouter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring-test.xml"})
public class ContainerContextTest {

    @Autowired
    private ApplicationContext ctx;

    @Test
    public void getBeanOk() {
        IContainerContext containerContext = new ContainerContext(ctx);
        BarRouter barRouter = containerContext.getBean(BarRouter.class);
        assertNotNull(barRouter);
        barRouter = containerContext.getBean("barRouter", BarRouter.class);
        assertNotNull(barRouter);
    }

    @Test
    public void getBeanFails() {
        IContainerContext containerContext = new ContainerContext(ctx);
        try {
            containerContext.getBean(Date.class);
            fail();
        } catch (NoSuchBeanDefinitionException expected) {
            assertEquals("No qualifying bean of type 'java.util.Date' available", expected.getMessage());
        }

        try {
            containerContext.getBean("unknown", String.class);
            fail();
        } catch (RuntimeException expected) {

        }

        try {
            containerContext.getBean(null);
            fail();
        } catch (NullPointerException expected) {

        }

        try {
            containerContext.getBean(null, String.class);
            fail();
        } catch (NullPointerException expected) {

        }

        try {
            containerContext.getBean(null, null);
            fail();
        } catch (NullPointerException expected) {

        }

        try {
            containerContext.getBean("xxx", null);
            fail();
        } catch (NullPointerException expected) {

        }
    }

}