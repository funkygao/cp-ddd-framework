package org.cdf.ddd.runtime.registry;

import org.cdf.ddd.runtime.DDD;
import org.cdf.ddd.runtime.registry.mock.partner.FooPartner;
import org.cdf.ddd.runtime.registry.mock.pattern.B2BPattern;
import org.cdf.ddd.runtime.registry.mock.pattern.FooPattern;
import org.junit.Test;

import static org.junit.Assert.*;

public class ContainerTest {

    @Test
    public void basic() {
        Container container = Container.getInstance();
        assertSame(container, Container.getInstance());
        assertSame(container, DDD.getContainer());

        try {
            Container.getInstance().loadPartner("a/b", null);
            fail();
        } catch (IllegalArgumentException expected) {
            assertEquals("Invalid jarPath: a/b", expected.getMessage());
        } catch (Exception unexpected) {
            fail();
        }
    }

    @Test
    public void unload() {
        DDD.getContainer().unloadPattern(B2BPattern.CODE);
        DDD.getContainer().unloadPartner(FooPartner.CODE);
    }

}