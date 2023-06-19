/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class KeyBehaviorEntry {
    private String className;
    private String methodName;
    private String realMethodName;
    private String packageName;
    private Set<String> rules = new HashSet<>();
    private Set<String> modes = new HashSet<>();
    private List<String> args = new ArrayList<>();
    private String remark = "";
    private String javadoc;

    public KeyBehaviorEntry(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
        this.realMethodName = methodName;
    }

    public String displayArgs() {
        if (args == null || args.isEmpty()) {
            return "";
        }

        return String.join(",", args);
    }

    public String displayNameWithRemark() {
        if (remark == null || remark.isEmpty()) {
            return methodName;
        }

        return methodName + "/" + remark;
    }
}
