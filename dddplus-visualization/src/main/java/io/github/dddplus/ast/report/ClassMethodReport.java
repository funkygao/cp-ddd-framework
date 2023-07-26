/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.report;

import io.github.dddplus.ast.model.ReverseEngineeringModel;
import lombok.Data;

import java.util.*;

/**
 * 类和方法的分布情况.
 */
@Data
public class ClassMethodReport {
    private final ReverseEngineeringModel model;

    private ClassInfo classInfo = new ClassInfo();
    private MethodInfo methodInfo = new MethodInfo();
    private int statementN = 0;
    private int fieldsN = 0;

    public ClassMethodReport(ReverseEngineeringModel model) {
        this.model = model;
    }

    public int publicClazzN() {
        return classInfo.publicClasses.size();
    }

    public int publicMethodN() {
        return methodInfo.publicMethods.size();
    }

    public Map<Integer, String> topTenBigMethods() {
        Map<Integer, String> result = new TreeMap<>(Collections.reverseOrder());
        int i = 0;
        for (Integer loc : methodInfo.getBigMethods().keySet()) {
            i++;
            if (i == 10) {
                break;
            }

            result.put(loc, methodInfo.getBigMethods().get(loc));
        }
        return result;
    }

    @Data
    public static class ClassInfo {
        private Set<String> publicClasses = new TreeSet<>();
        private Set<String> innerClasses = new TreeSet<>();
        private Set<String> packageVisibleClasses = new TreeSet<>();
        private Set<String> deprecatedClasses = new TreeSet<>();
        private Set<String> genericClasses = new TreeSet<>();
        private Set<String> interfaces = new TreeSet<>();
        private Set<String> abstractClasses = new TreeSet<>();
    }

    @Data
    public static class MethodInfo {
        private Set<String> privateMethods = new TreeSet<>();
        private Set<String> defaultMethods = new TreeSet<>();
        private Set<String> publicMethods = new TreeSet<>();
        private Set<String> protectedMethods = new TreeSet<>();
        private Set<String> abstractMethods = new TreeSet<>();
        private Set<String> staticMethods = new TreeSet<>();
        private Set<String> deprecatedMethods = new TreeSet<>();
        private Map<Integer, String> bigMethods = new TreeMap<>(Collections.reverseOrder());
    }

    public void incrStatement() {
        this.statementN += 1;
    }

    public void incrField() {
        this.fieldsN += 1;
    }
}
