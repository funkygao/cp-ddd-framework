/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.model;

import com.github.javaparser.Position;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Data
public class KeyFlowEntry {
    private final String className;
    private final String realMethodName;
    private final String javadoc;
    private String actor;
    private String methodName;
    private Set<String> events = new TreeSet<>(); // 该流程产生哪些领域事件
    private List<String> args;
    private Set<String> realArguments;
    private String remark;
    private Position position;
    private String absolutePath;
    private boolean async = false;
    private boolean polymorphism = false;
    private boolean useRawArgs = false;
    private boolean usecase = false;
    private boolean nonPublic = false;

    public KeyFlowEntry(String className, String realMethodName, String javadoc) {
        this.className = className;
        this.realMethodName = realMethodName;
        this.methodName = realMethodName;
        this.javadoc = javadoc;

        this.actor = "";
    }

    public String getSortedKey() {
        if (usecase) {
            return "_" + methodName; // usecase排在前面
        }

        if (nonPublic) {
            return "~" + methodName; // 非public方法排在后面
        }

        return methodName;
    }

    public String actor() {
        if (actor.isEmpty()) {
            return className;
        }

        return actor;
    }

    public boolean produceEvent() {
        return events != null && !events.isEmpty();
    }

    public String displayEvents() {
        return String.join(",", events);
    }

    /**
     * 没有被{@link AggregateEntry}收录到{@link KeyModelEntry}.
     */
    public boolean isOrphan() {
        return !className.equals(actor);
    }

    public String plainDisplayActualClass() {
        if (!actor.equals(className)) {
            return className;
        }

        return "";
    }

    public String umlDisplayActualClass() {
        if (!actor.equals(className)) {
            if (position != null && absolutePath != null) {
                // [[http://www.google.com theLabel]]
                // IDEA Settings:
                // Build, Execution, Deployment | Debugger
                // Allow unsigned requests, check it
                return String.format("[[http://localhost:63342/api/file/%s:%d %s]]",
                        absolutePath, position.line, className);
            }

            return className;
        }

        return "";
    }

    private String displayArgs() {
        if (useRawArgs) {
            return String.join(",", realArguments);
        }

        if (args == null || args.isEmpty()) {
            return "";
        }

        return String.join(",", args);
    }

    public String displayEffectiveArgs() {
        String args = displayArgs();
        List<String> l = new ArrayList<>();
        if (realArguments != null && !realArguments.isEmpty()) {
            l.addAll(realArguments);
        }
        if (!args.isEmpty()) {
            l.add(args);
        }
        return String.join(",", l);
    }

    public String displayNameWithRemark() {
        if (remark == null || remark.isEmpty()) {
            return methodName;
        }

        return methodName + "/" + remark;
    }
}
