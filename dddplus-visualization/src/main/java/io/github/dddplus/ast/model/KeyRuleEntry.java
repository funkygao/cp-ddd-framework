/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.model;

import lombok.Data;

import java.util.Set;

@Data
public class KeyRuleEntry {
    private String className;
    private String methodName;
    private String actor = "";
    private String realMethodName;
    private String remark;
    private Set<String> refer;
    private String javadoc;

    public String actor() {
        if (actor.isEmpty()) {
            return className;
        }

        return actor;
    }

    public String displayRefer() {
        if (refer == null || refer.isEmpty()) {
            return "";
        }

        return String.join(",", refer);
    }

    public String displayNameWithRemark() {
        if (remark == null || remark.isEmpty()) {
            return methodName;
        }

        return methodName + "/" + remark;
    }
}
