/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class EncapsulationReport {
    private static final String NEWLINE = System.getProperty("line.separator");

    // {class: publicMethodSet}
    Map<String, List<String>> result = new TreeMap<>();

    public void registerMethodInfo(String classInfo, String methodInfo) {
        if (!result.containsKey(classInfo)) {
            result.put(classInfo, new ArrayList<>());
        }

        result.get(classInfo).add(methodInfo);
    }

    public String content() {
        StringBuilder sb = new StringBuilder();
        for (String classInfo : result.keySet()) {
            sb.append(classInfo).append(NEWLINE);
            for (String methodInfo : result.get(classInfo)) {
                sb.append("    ").append(methodInfo).append(NEWLINE);
            }
        }
        return sb.toString();
    }
}
