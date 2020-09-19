package org.cdf.ddd.runtime.registry;

import org.cdf.ddd.annotation.Domain;
import org.junit.Test;

import static org.junit.Assert.*;

public class JarDynamicLoaderTest {

    @Test
    public void loadNonPatternNorPartner() throws Exception {
        JarDynamicLoader loader = new JarDynamicLoader();
        try {
            loader.load("", "", Domain.class, null);
            fail();
        } catch (IllegalArgumentException expected) {
            assertEquals("Must be Pattern or Partner", expected.getMessage());
        }
    }

}