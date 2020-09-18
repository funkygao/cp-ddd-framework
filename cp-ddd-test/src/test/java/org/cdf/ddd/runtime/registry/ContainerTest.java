package org.cdf.ddd.runtime.registry;

import org.cdf.ddd.runtime.DDD;
import org.junit.Test;

import static org.junit.Assert.*;

public class ContainerTest {

    @Test
    public void basic() {
        Container container = Container.getInstance();
        assertSame(container, Container.getInstance());
        assertSame(container, DDD.getContainer());

        assertEquals("order-center-bp-isv-0.0.1.jar", container.jarName("../cp-ddd-example/order-center-bp-isv/target/order-center-bp-isv-0.0.1.jar"));
        assertEquals("a.jar", container.jarName("a.jar"));

        try {
            Container.getInstance().loadPartner("a/b", null);
            fail();
        } catch (IllegalArgumentException expected) {
            assertEquals("Invalid jarPath: a/b", expected.getMessage());
        } catch (Exception unexpected) {
            fail();
        }
    }

}