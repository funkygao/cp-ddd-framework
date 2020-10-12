package io.github.dddplus.runtime;

import org.junit.Test;

import static org.junit.Assert.*;

public class ExtTimeoutExceptionTest {

    @Test
    public void getMessage() {
        ExtTimeoutException extTimeoutException = new ExtTimeoutException(5000);
        assertEquals("timeout:5000ms", extTimeoutException.getMessage());
    }

}