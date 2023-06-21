/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.model;

import lombok.Data;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class KeyPropertyEntry {
    private String className;
    private String name;
    private String realName;
    private String remark;
    private String javadoc;

    public String displayName() {
        Set<String> parts = new LinkedHashSet<>();
        parts.add(name);
        if (false && javadoc != null && !javadoc.isEmpty()) {
            // some javadoc is too long, ugly to display
            parts.add(javadoc);
        }
        if (remark != null && !remark.isEmpty()) {
            parts.add(remark);
        }

        return String.join("/", parts);
    }
}
