/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.model;

import io.github.dddplus.dsl.KeyBehavior;
import lombok.Data;

@Data
public class KeyEventEntry {
    private String className;
    private String remark;
    private String javadoc;

    /**
     * 该事件是否没有找到生产者.
     *
     * <p>生产者是通过{@link KeyBehavior#produceEvent()}标注的.</p>
     */
    private boolean orphan;

    public boolean hasRemark() {
        return remark != null && !remark.isEmpty();
    }

    public boolean orphaned() {
        return orphan;
    }
}
