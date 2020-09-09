package org.ddd.cp.ddd.runtime.registry;

import org.ddd.cp.ddd.runtime.DDD;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@Slf4j
public class PartnerClassLoaderTest {

    @Test
    public void loadClass() throws Exception {
        PartnerClassLoader loader = new PartnerClassLoader(new URL[]{new File("").toURI().toURL()});
        Class clazz = loader.loadClass(PartnerClassLoaderTest.class.getCanonicalName(), false);
        assertTrue(clazz.equals(PartnerClassLoaderTest.class));
    }

    @Test
    public void jdkClassLoader() {
        ClassLoader classLoader = PartnerClassLoader.jdkClassLoader();
        assertNotNull(classLoader);
        assertTrue(classLoader instanceof URLClassLoader);
    }

    @Test
    public void platformClassLoader() {
        ClassLoader classLoader = PartnerClassLoader.platformClassLoader();
        assertNotNull(classLoader);
        assertEquals("sun.misc.Launcher.AppClassLoader", classLoader.getClass().getCanonicalName());
    }

    @Test
    public void platformFirstClass() throws MalformedURLException {
        PartnerClassLoader loader = new PartnerClassLoader(new URL[]{new File("").toURI().toURL()});
        assertTrue(loader.platformFirstClass(DDD.class.getName()));
        assertFalse(loader.platformFirstClass(List.class.getName()));
        assertFalse(loader.platformFirstClass("com.jdl.bp.oms.doo.j.extension.JAntiConcurrentLockExt"));
    }

    @Test
    public void playWithClassLoader() {
        log.info("ClassLoader\nthis={} log={} ArrayList={}",
                PartnerLoaderTest.class.getClassLoader(),
                log.getClass().getClassLoader(),
                ArrayList.class.getClassLoader());
    }
}