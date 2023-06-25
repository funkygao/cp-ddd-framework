/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.model;

import io.github.dddplus.dsl.KeyEvent;
import lombok.Data;

@Data
public class KeyEventEntry {
    private String className;
    private KeyEvent.Type type;
    private String remark;
    private String javadoc;

    public boolean hasRemark() {
        return remark != null && !remark.isEmpty();
    }
}
