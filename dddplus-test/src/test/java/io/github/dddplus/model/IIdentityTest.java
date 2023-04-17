package io.github.dddplus.model;

import io.github.dddplus.runtime.registry.mock.model.FooModel;
import io.github.dddplus.runtime.registry.mock.pattern.B2BPattern;
import io.github.dddplus.runtime.registry.mock.pattern.B2CPattern;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class IIdentityTest {

    @Test
    public void matchAny() {
        FooModel identity = new FooModel();
        assertTrue(identity.matchAny(new B2BPattern(), new B2CPattern()));
    }

}