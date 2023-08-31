package io.github.dddplus.bce;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class CallGraphConfigTest {

    @Test
    void fromFile() throws FileNotFoundException {
        try {
            CallGraphConfig.fromFile("../doc/none");
            fail();
        } catch (FileNotFoundException expected) {

        }

        CallGraphConfig config = CallGraphConfig.fromFile("../doc/callgraph.json");
        assertTrue(config.getIgnore().getEnumClazz());
        assertNotNull(config.getIgnore().getCalleeMethods());
        assertNotNull(config.getIgnore().getCalleePackagesLike());
        assertNotNull(config.getIgnore().getClassSuffix());
    }

}