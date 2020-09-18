/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.cdf.ddd.runtime.registry;

import org.cdf.ddd.plugin.IContainerContext;

import java.lang.annotation.Annotation;

class ContainerContext implements IContainerContext {

    @Override
    public void registerBean(Class<? extends Annotation> annotation, Object object) throws Exception {
        if (!RegistryFactory.lazyRegister(annotation, object)) {
            throw new RuntimeException("Unsupported annotation: " + annotation.getCanonicalName());
        }
    }

    @Override
    public void deregisterBean(Class<? extends Annotation> annotation, Object object) throws Exception {
        RegistryFactory.lazyDeregister(annotation, object);
    }
}
