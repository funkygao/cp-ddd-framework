/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.runtime.registry;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.support.AopUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

final class InternalAopUtils {
    private InternalAopUtils() {}

    static <T extends Annotation> T getAnnotation(Object bean, Class<T> annotationClazz) {
        Annotation annotation;
        annotation = bean.getClass().getAnnotation(annotationClazz);
        if (annotation == null && AopUtils.isAopProxy(bean)) {
            Class clz = AopUtils.getTargetClass(bean);
            annotation = clz.getAnnotation(annotationClazz);
        }

        return (T) annotation;
    }

    static Object getTarget(Object bean) throws BootstrapException {
        if (!AopUtils.isAopProxy(bean)) {
            return bean;
        } else if (AopUtils.isCglibProxy(bean)) {
            try {
                Field h = bean.getClass().getDeclaredField("CGLIB$CALLBACK_0");
                h.setAccessible(true);
                Object dynamicAdvisedInterceptor = h.get(bean);
                Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
                advised.setAccessible(true);
                return ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
            } catch (Exception e) {
                throw BootstrapException.ofMessage(e.getMessage());
            }
        } else {
            throw BootstrapException.ofMessage("Unable to handle the AOP proxy!");
        }
    }

}
