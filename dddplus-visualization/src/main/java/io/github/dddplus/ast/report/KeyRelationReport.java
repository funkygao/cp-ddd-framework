/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.report;

import io.github.dddplus.ast.model.KeyRelationEntry;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class KeyRelationReport {
    private List<KeyRelationEntry> relationEntries = new ArrayList<>();

    public KeyRelationReport add(KeyRelationEntry entry) {
        relationEntries.add(entry);
        return this;
    }

    public int size() {
        return relationEntries.size();
    }

}
