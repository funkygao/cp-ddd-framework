package org.cdf.ddd.runtime.registry;

import lombok.extern.slf4j.Slf4j;
import org.cdf.ddd.runtime.DDD;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import static org.junit.Assert.*;

@Slf4j
public class PluginClassLoaderTest {

    @Test
    public void loadClass() throws Exception {
        PluginClassLoader loader = new PluginClassLoader(new URL[]{new File("").toURI().toURL()});
        Class clazz = loader.loadClass(PluginClassLoaderTest.class.getCanonicalName(), false);
        assertTrue(clazz.equals(PluginClassLoaderTest.class));
    }

    @Test
    public void jdkClassLoader() {
        ClassLoader classLoader = PluginClassLoader.jdkClassLoader();
        assertNotNull(classLoader);
        assertTrue(classLoader instanceof URLClassLoader);
    }

    @Test
    public void containerClassLoader() {
        ClassLoader classLoader = PluginClassLoader.containerClassLoader();
        assertNotNull(classLoader);
        assertEquals("sun.misc.Launcher.AppClassLoader", classLoader.getClass().getCanonicalName());
    }

    @Test
    public void containerFirstClass() throws MalformedURLException {
        PluginClassLoader loader = new PluginClassLoader(new URL[]{new File("").toURI().toURL()});
        assertTrue(loader.containerFirstClass(DDD.class.getName()));
        assertFalse(loader.containerFirstClass(List.class.getName()));
        assertFalse(loader.containerFirstClass("com.jdl.bp.oms.doo.j.extension.JAntiConcurrentLockExt"));
    }

}