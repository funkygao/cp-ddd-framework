package io.github.dddplus.ast.report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    public void dump(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.append(content());
        }
    }

    private String content() {
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
