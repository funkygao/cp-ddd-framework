package org.cdf.ddd.runtime.registry;

import org.cdf.ddd.plugin.IContainerContext;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ContainerContextTest {

    @Test
    public void basic() {
        ContainerContext ctx = new ContainerContext();
        assertTrue(ctx instanceof IContainerContext);
    }

}