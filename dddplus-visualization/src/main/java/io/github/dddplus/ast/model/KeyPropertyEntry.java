/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.model;

import io.github.dddplus.dsl.KeyElement;
import lombok.Data;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class KeyPropertyEntry implements Comparable<KeyPropertyEntry> {
    private String className;
    private String name;
    private String realName;
    private String remark;
    private String javadoc;

    public String displayName(KeyElement.Type type) {
        Set<String> parts = new LinkedHashSet<>();
        if (type == KeyElement.Type.Referential) {
            parts.add(String.format("<color:Red>%s</color>", name));
        } else {
            parts.add(name);
        }
        if (remark != null && !remark.isEmpty()) {
            parts.add(remark);
        }

        return String.join("/", parts);
    }

    @Override
    public int compareTo(KeyPropertyEntry that) {
        return name.compareTo(that.name);
    }
}
