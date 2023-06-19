/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class KeyFlowEntry {
    private String className;
    private String actor;
    private String methodName;
    private Set<String> rules;
    private Set<String> modes;
    private List<String> args;
    private Set<String> initiators;
    private String javadoc;
    private String remark;
    private String realMethodName;

    public String actor() {
        if (actor.isEmpty()) {
            return className;
        }

        return actor;
    }

    /**
     * 没有被{@link AggregateEntry}收录到{@link KeyModelEntry}.
     */
    public boolean isOrphan() {
        return !className.equals(actor);
    }

    public String displayActualClass() {
        if (!actor.equals(className)) {
            return className;
        }

        return "";
    }

    private String displayArgs() {
        if (args == null || args.isEmpty()) {
            return "";
        }

        return String.join(",", args);
    }

    public String displayArgsWithRules() {
        String args = displayArgs();
        String rules = displayRules();
        List<String> l = new ArrayList<>();
        if (!args.isEmpty()) {
            l.add(args);
        }
        if (!rules.isEmpty()) {
            l.add(rules);
        }
        return String.join(",", l);
    }

    private String displayRules() {
        if (rules == null || rules.isEmpty()) {
            return "";
        }

        return String.join(",", rules);
    }

    public String displayNameWithRemark() {
        if (remark == null || remark.isEmpty()) {
            return methodName;
        }

        return methodName + "/" + remark;
    }
}
