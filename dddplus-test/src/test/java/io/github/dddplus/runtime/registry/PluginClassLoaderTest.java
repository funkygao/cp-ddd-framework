package io.github.dddplus.runtime.registry;

import lombok.extern.slf4j.Slf4j;
import io.github.dddplus.runtime.DDD;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.*;

@Slf4j
public class PluginClassLoaderTest {

    @Test
    public void containerFirstClass() throws MalformedURLException {
        PluginClassLoader loader = new PluginClassLoader(new URL[]{new File("").toURI().toURL()}, null, null);
        assertTrue(loader.containerFirstClass(DDD.class.getName()));
        assertFalse(loader.containerFirstClass(List.class.getName()));
        assertFalse(loader.containerFirstClass("com.ddd.bp.oms.doo.j.extension.JAntiConcurrentLockExt"));
    }

    @Test
    public void containerClassLoader() {
        assertSame(this.getClass().getClassLoader(), Container.class.getClassLoader());
    }

    @Test
    public void classLoader() {
        log.info("default class loader: {}", this.getClass().getClassLoader());
        // sun.misc.Launcher$AppClassLoader，也被称为 SystemClassLoader
        assertSame(this.getClass().getClassLoader(), ClassLoader.getSystemClassLoader());

        printClassLoaderTree(new PluginClassLoaderTest());
    }

    private void printClassLoaderTree(Object target) {
        ClassLoader classLoader = target.getClass().getClassLoader();
        while (classLoader != null) {
            System.out.println(classLoader);
            classLoader = classLoader.getParent();
        }
    }

}
