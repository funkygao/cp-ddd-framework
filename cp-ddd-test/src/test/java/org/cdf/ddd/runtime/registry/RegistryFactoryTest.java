package org.cdf.ddd.runtime.registry;

import org.cdf.ddd.runtime.registry.mock.extension.B2BExt;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring-test.xml"})
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