package io.github.dddplus.runtime.registry;

import io.github.dddplus.testing.LogAssert;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.*;

@Slf4j
public class ContainerTest {

    @Test
    public void basic() {
        Container container = Container.getInstance();
        assertSame(container, Container.getInstance());

        assertNotNull(container.getActivePlugins());

        try {
            Container.getInstance().loadPartnerPlugin("foo", "v1", "a/b", true);
            fail();
        } catch (IllegalArgumentException expected) {
            assertEquals("Invalid jarPath: a/b", expected.getMessage());
        } catch (Throwable unexpected) {
            fail();
        }
    }

    @Test
    public void jarNotExist() throws IOException {
        try {
            Container.getInstance().loadPartnerPlugin("foo", "v1", "a.jar", false);
            fail();
        } catch (FileNotFoundException expected) {
        } catch (Throwable unexpected) {
            fail();
        }

        LogAssert.assertContains("Loading partner:a.jar useSpring:false", "fails to load partner:a.jar, cost");
    }

    @Test
    public void createLocalFile() throws Exception {
        URL jarUrl = new URL("https://github.com/funkygao/dddplus/blob/master/doc/assets/jar/order-center-bp-isv-0.0.1.jar");
        File file = Container.getInstance().createLocalFile(jarUrl);
        file.deleteOnExit();
        log.info("{}", file.getCanonicalFile());
        assertTrue(file.getCanonicalPath().endsWith(".jar"));
        file.delete();

        jarUrl = new URL("https://github.com/funkygao/dddplus/blob/master/doc/assets/jar/order-center-bp-isv-0.0.1.jar?raw=true");
        file = Container.getInstance().createLocalFile(jarUrl);
        file.deleteOnExit();
        assertTrue(file.getCanonicalPath().endsWith(".jar"));
        file.delete();
    }

}