package org.cdf.ddd.runtime.registry;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PartnerLoaderTest {

    @Test
    @Ignore
    public void start() throws Exception {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("ddd-example-test.xml");
        context.start();
        PartnerLoader fooJar = new PartnerLoader();
        fooJar.load("../cp-ddd-example/order-center-bp-isv/target/order-center-bp-isv-0.0.1.jar", "org.example.bp");
        context.stop();
    }

    @Test
    public void jarPath() throws Exception {
        try {
            new PartnerLoader().load("a/b", null);
            fail();
        } catch (IllegalArgumentException expected) {
            assertEquals("Invalid jarPath: a/b", expected.getMessage());
        }
    }

    @Test
    public void jarName() {
        PartnerLoader loader = new PartnerLoader();
        assertEquals("order-center-bp-isv-0.0.1.jar", loader.jarName("../cp-ddd-example/order-center-bp-isv/target/order-center-bp-isv-0.0.1.jar"));

        loader = new PartnerLoader();
        assertEquals("a.jar", loader.jarName("a.jar"));
    }

}
