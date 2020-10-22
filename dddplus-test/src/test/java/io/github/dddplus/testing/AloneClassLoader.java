/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.testing;

import java.net.URLClassLoader;

// https://github.com/johnyannj/junit-alone
public class AloneClassLoader extends URLClassLoader {
    private ClassLoader appClassLoader;

    public AloneClassLoader() {
        super(((URLClassLoader) getSystemClassLoader()).getURLs(),
                Thread.currentThread().getContextClassLoader().getParent());
        appClassLoader = Thread.currentThread().getContextClassLoader();
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (name.startsWith("org.junit.") || name.startsWith("junit.")) {
            return appClassLoader.loadClass(name);
        }

        return super.loadClass(name);
    }
}
