/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.bce;

import io.github.dddplus.ast.report.CallGraphReport;
import lombok.extern.slf4j.Slf4j;
import org.apache.bcel.classfile.ClassParser;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Slf4j
public class CallGraphParser {

    public static CallGraphReport parse(String[] jarPaths, CallGraphConfig config) throws IOException {
        CallGraphReport report = new CallGraphReport(config);
        for (String jarPath : jarPaths) {
            File jarFile = new File(jarPath);
            if (!jarFile.exists()) {
                throw new RuntimeException("Jar file " + jarPath + " does not exist");
            }

            try (JarFile jar = new JarFile(jarFile)) {
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = entries.nextElement();
                    if (jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")) {
                        // e,g. xml, properties, etc
                        continue;
                    }

                    log.debug("parsing {}", jarEntry.getName());
                    ClassParser classParser = new ClassParser(jarPath, jarEntry.getName());
                    new ClassVisitor(classParser.parse(), report, config)
                            .start();
                }
            }
        }

        return report;
    }
}
