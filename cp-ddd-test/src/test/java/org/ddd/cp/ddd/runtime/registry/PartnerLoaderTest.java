package org.ddd.cp.ddd.runtime.registry;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PartnerLoaderTest {

    @Test
    @Ignore
    public void start() throws Exception {
        PartnerLoader fooJar = new PartnerLoader("");
        fooJar.load();
        fooJar.unload();
    }

    @Test
    public void unload() throws Exception {
        PartnerLoader fooJar = new PartnerLoader("");
        // 还没有load就unload，会打印warning log
        fooJar.unload();
    }

    @Test
    public void label() {
        PartnerLoader foo = new PartnerLoader("a/b/c");
        assertEquals("PartnerLoader(jarPath=a/b/c)", foo.label());
    }

}
