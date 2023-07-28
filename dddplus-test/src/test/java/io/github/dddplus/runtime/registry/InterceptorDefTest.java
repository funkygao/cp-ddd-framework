package io.github.dddplus.runtime.registry;

import org.junit.Assert;
import org.junit.Test;

public class InterceptorDefTest {

    @Test
    public void basic() {
        InterceptorDef interceptorDef = new InterceptorDef();
        try {
            interceptorDef.registerBean(null);
            Assert.fail();
        } catch (NullPointerException expected) {

        }
    }

}
