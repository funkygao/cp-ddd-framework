/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.model;

import lombok.Data;

import java.util.*;

@Data
public class KeyBehaviorEntry {
    private String className;
    private String methodName;
    private String realMethodName;
    private String packageName;
    private List<String> args = new ArrayList<>();
    private Set<String> realArguments = new TreeSet<>();
    private Set<String> events = new TreeSet<>(); // 该行为产生哪些领域事件
    private String remark = "";
    private String javadoc;
    private boolean async = false;
    private boolean useRawArgs = false;
    private boolean abstracted = false;

    public KeyBehaviorEntry(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
        this.realMethodName = methodName;
    }

    public boolean produceEvent() {
        return events != null && !events.isEmpty();
    }

    public String displayEvents() {
        return String.join(",", events);
    }

    public String displayArgs() {
        if (useRawArgs) {
            return String.join(",", realArguments);
        }

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
