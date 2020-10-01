package org.cdf.ddd.runtime.registry;

import org.cdf.ddd.runtime.registry.mock.extension.B2BExt;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class RegistryFactoryTest {

    @Test
    public void lazyRegisterInvalid() {
        try {
            RegistryFactory.lazyRegister(Resource.class, new B2BExt());
            fail();
        } catch (BootstrapException expected) {
            assertEquals("Unsupported type: javax.annotation.Resource", expected.getMessage());
        }
    }
}