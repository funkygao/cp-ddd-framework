/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.model;

import lombok.Data;

@Data
public class KeyPropertyEntry {
    private String className;
    private String name;
    private String realName;
    private String remark;
    private String javadoc;

    public boolean x() {
        return !name.equals(realName);
    }

    public String displayNameWithRemark() {
        if (remark == null || remark.isEmpty()) {
            return name;
        }

        return name + "(" + remark + ")";
    }
}
