/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.cdf.ddd.runtime.registry;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Slf4j
class JarUtils {

    static Map<Class<? extends Annotation>, List<Class>> loadClassWithAnnotations(String path, List<Class<? extends Annotation>> annotations, String startWith, ClassLoader loader) throws Exception {
        Map<Class<? extends Annotation>, List<Class>> result = new HashMap<>();
        List<String> classes = filter(getAllClasses(path), startWith);
        for (String className : classes) {
            // 把.class文件中的二进制数据读入堆里的Class对象
            log.info("loading {} with {}", className, loader);
            Class clazz = loader.loadClass(className);
            for (Class<? extends Annotation> annotation : annotations) {
                Annotation clazzAnnotation = clazz.getAnnotation(annotation);
                if (clazzAnnotation == null) {
                    // 该Class没有该注解
                    continue;
                }

                List<Class> annotationClassList = result.get(annotation);
                if (annotationClassList == null) {
                    annotationClassList = new ArrayList<>();
                    result.put(annotation, annotationClassList);
                }

                annotationClassList.add(clazz);
            }
        }

        return result;
    }

    private static String getClassName(JarEntry jarEntry) {
        String jarName = jarEntry.getName();
        if (!jarName.endsWith(".class")) {
            return null;
        } else {
            if (jarName.charAt(0) == 47) {
                jarName = jarName.substring(1);
            }

            jarName = jarName.replace("/", ".");
            return jarName.substring(0, jarName.length() - 6);
        }
    }

    private static List<String> getAllClasses(String path) throws Exception {
        ArrayList classNames = new ArrayList();
        JarFile jar = new JarFile(new File(path)); // might throw FileNotFoundException

        Enumeration entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = (JarEntry) entries.nextElement();
            String className = getClassName(entry);
            if (className != null && className.length() > 0) {
                classNames.add(className);
            }
        }

        return classNames;
    }

    private static List<String> filter(List<String> names, String startWith) {
        if (startWith == null || startWith.isEmpty()) {
            return names;
        }

        List<String> result = new ArrayList(names.size());
        for (String name : names) {
            if (name == null) {
                continue;
            }

            if (name.startsWith(startWith)) {
                result.add(name);
            }
        }

        return result;
    }

}
