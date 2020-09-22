package org.cdf.ddd.runtime.registry;

import lombok.extern.slf4j.Slf4j;
import org.cdf.ddd.runtime.DDD;
import org.cdf.ddd.runtime.registry.mock.partner.FooPartner;
import org.cdf.ddd.runtime.registry.mock.pattern.B2BPattern;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.*;

@Slf4j
public class ContainerTest {

    @Test
    public void basic() {
        Container container = Container.getInstance();
        assertSame(container, Container.getInstance());
        assertSame(container, DDD.getContainer());

        try {
            Container.getInstance().loadPartnerPlugin("a/b", null);
            fail();
        } catch (IllegalArgumentException expected) {
            assertEquals("Invalid jarPath: a/b", expected.getMessage());
        } catch (Exception unexpected) {
            fail();
        }
    }

    @Test
    public void unload() {
        DDD.getContainer().unloadPatternPlugin(B2BPattern.CODE);
        DDD.getContainer().unloadPartnerPlugin(FooPartner.CODE);
    }

    @Test
    public void javaFileCreateTempFile() throws IOException {
        File file = File.createTempFile("axx", ".jar");
        String path = file.getCanonicalPath();
        log.info("temp file: {} {}", file.getName(), path); // on Mac /private/var/folders/bn/c4wnyc0d69n2wdytytt8xz8w0000gp/T/axx8833496648090636081b
        assertTrue(file.getName().startsWith("axx"));
        assertTrue(path.endsWith(".jar"));
        file.delete();
    }

    @Test
    public void jarTempLocalFile() throws Exception {
        URL jarUrl = new URL("https://github.com/funkygao/cp-ddd-framework/blob/master/doc/assets/jar/order-center-bp-isv-0.0.1.jar");
        File file = Container.getInstance().jarTempLocalFile(jarUrl);
        file.deleteOnExit();
        log.info("{}", file.getCanonicalFile());
        assertTrue(file.getCanonicalPath().endsWith(".jar"));
        file.delete();

        jarUrl = new URL("https://github.com/funkygao/cp-ddd-framework/blob/master/doc/assets/jar/order-center-bp-isv-0.0.1.jar?raw=true");
        file = Container.getInstance().jarTempLocalFile(jarUrl);
        file.deleteOnExit();
        assertTrue(file.getCanonicalPath().endsWith(".jar"));
        file.delete();
    }

}