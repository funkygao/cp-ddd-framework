/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.model;

import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.dsl.KeyEvent;
import lombok.Data;

@Data
public class KeyEventEntry {
    private String className;
    private KeyEvent.Type type;
    private String remark;
    private String javadoc;

    /**
     * 该事件是否没有找到生产者.
     *
     * <p>只有{@link KeyEvent.Type#RemoteProducing}和{@link KeyEvent.Type#Local}才有效.</p>
     * <p>生产者是通过{@link KeyBehavior#produceEvent()}标注的.</p>
     */
    private boolean orphan;

    public boolean hasRemark() {
        return remark != null && !remark.isEmpty();
    }

    public boolean orphaned() {
        if (type == KeyEvent.Type.RemoteConsuming) {
            return false;
        }

        return orphan;
    }
}
