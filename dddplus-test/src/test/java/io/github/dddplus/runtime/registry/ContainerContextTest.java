package io.github.dddplus.runtime.registry;

import io.github.dddplus.plugin.IContainerContext;
import io.github.dddplus.runtime.registry.mock.ability.BarDomainAbility;
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
        BarDomainAbility barDomainAbility = containerContext.getBean(BarDomainAbility.class);
        assertNotNull(barDomainAbility);
        barDomainAbility = containerContext.getBean("barDomainAbility", BarDomainAbility.class);
        assertNotNull(barDomainAbility);
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
    }

}