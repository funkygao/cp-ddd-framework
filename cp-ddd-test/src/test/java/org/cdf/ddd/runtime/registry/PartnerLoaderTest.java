package org.cdf.ddd.runtime.registry;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.assertEquals;

public class PartnerLoaderTest {

    @Test
    @Ignore
    public void start() throws Exception {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("ddd-example-test.xml");
        context.start();
        PartnerLoader fooJar = new PartnerLoader("../cp-ddd-example/order-center-bp-isv/target/order-center-bp-isv-0.0.1.jar", "org.example.bp");
        fooJar.load();
        fooJar.unload();
        context.stop();
    }

    @Test
    public void label() {
        PartnerLoader foo = new PartnerLoader("a/b/c", "");
        assertEquals("PartnerLoader(jarPath=a/b/c)", foo.label());
    }

}
