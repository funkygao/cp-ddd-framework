/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.model;

import io.github.dddplus.dsl.KeyRelation;
import lombok.Data;

@Data
public class KeyRelationEntry {
    private String leftClass;
    private String rightClass;
    private KeyRelation.Type type;
    private String remark;
    private String javadoc;

    public void setTypeInString(String typeStr) {
        this.type = KeyRelation.Type.valueOf(typeStr);
    }

    public String displayRemark() {
        if (remark == null) {
            return "";
        }

        return remark;
    }

    public boolean sameAs(KeyRelationEntry that) {
        return leftClass.equals(that.getLeftClass())
                && rightClass.equals(that.getRightClass())
                && type.equals(that.getType());
    }
}
