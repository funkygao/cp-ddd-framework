package io.github.dddplus.runtime.registry;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BootstrapExceptionTest {

    @Test
    public void ofMessage() {
        BootstrapException exception = BootstrapException.ofMessage("duplicated domain code: ", "CoreDomain");
        assertEquals("duplicated domain code: CoreDomain", exception.getMessage());
    }

}