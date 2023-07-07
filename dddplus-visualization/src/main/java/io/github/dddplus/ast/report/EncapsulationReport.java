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

    public void registerMethodInfo(String className, String methodInfo) {
        if (!result.containsKey(className)) {
            result.put(className, new ArrayList<>());
        }

        result.get(className).add(methodInfo);
    }

    public void dump(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.append(content());
        }
    }

    private String content() {
        StringBuilder sb = new StringBuilder();
        for (String className : result.keySet()) {
            sb.append(className).append(NEWLINE);
            for (String methodInfo : result.get(className)) {
                sb.append("    ").append(methodInfo).append(NEWLINE);
            }
        }
        return sb.toString();
    }
}
