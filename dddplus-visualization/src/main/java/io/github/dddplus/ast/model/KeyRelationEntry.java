/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.model;

import io.github.dddplus.dsl.KeyRelation;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class KeyRelationEntry {
    private String leftClass;
    private String rightClass;
    private KeyRelation.Type type;
    private String remark;
    private String leftClassPackageName;
    private String rightClassPackageName;
    private String javadoc;
    private boolean contextual = false;

    public void setTypeInString(String typeStr) {
        this.type = KeyRelation.Type.valueOf(typeStr);
    }

    public String displayRemark() {
        List<String> l = new ArrayList<>();

        if (contextual) {
            l.add("Contextual");
        }
        if (remark != null && !remark.isEmpty()) {
            l.add(remark);
        }

        if (l.isEmpty()) {
            return "";
        }

        return String.join(" ", l);
    }

    public boolean sameAs(KeyRelationEntry that) {
        return leftClass.equals(that.getLeftClass())
                && rightClass.equals(that.getRightClass())
                && type.equals(that.getType());
    }
}
