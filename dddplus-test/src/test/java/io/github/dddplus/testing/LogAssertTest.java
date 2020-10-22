package io.github.dddplus.testing;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;

import static io.github.dddplus.testing.LogAssert.assertContains;
import static io.github.dddplus.testing.LogAssert.assertNotContains;
import static org.junit.Assert.fail;

@Slf4j
public class LogAssertTest {

    @Test
    public void contains() throws IOException {
        log.info("Hello");
        assertNotContains("a");
        assertNotContains("Hello"); // LogAssert是有状态的，每次的assert都会读到EOF
        log.info("World");
        assertContains("World");
        assertNotContains("Hello", "World");

        log.info("DDDplus Framework");
        assertContains("DDD"); // partial match

        log.info("a");
        log.info("b");
        assertContains("a", "b");

        try {
            assertContains("a");
            fail();
        } catch (AssertionError expected) {

        }
    }

}
