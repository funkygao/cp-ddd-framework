package org.cdf.ddd.runtime.registry;

import org.cdf.ddd.annotation.Domain;
import org.junit.Test;

import static org.junit.Assert.*;

public class PluginLoaderTest {

    @Test
    public void loadNonPatternNorPartner() throws Exception {
        PluginLoader loader = new PluginLoader();
        try {
            loader.load("", "", Domain.class, null);
            fail();
        } catch (IllegalArgumentException expected) {
            assertEquals("Must be Pattern or Partner", expected.getMessage());
        }
    }

}