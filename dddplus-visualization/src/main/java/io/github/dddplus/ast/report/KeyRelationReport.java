/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.report;

import io.github.dddplus.ast.model.ReverseEngineeringModel;
import io.github.dddplus.ast.model.KeyModelEntry;
import io.github.dddplus.ast.model.KeyRelationEntry;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class KeyRelationReport {
    private final ReverseEngineeringModel model;

    private List<KeyRelationEntry> relationEntries = new ArrayList<>();

    public KeyRelationReport(ReverseEngineeringModel model) {
        this.model = model;
    }

    public KeyRelationReport add(KeyRelationEntry entry) {
        // dup check
        for (KeyRelationEntry relationEntry : relationEntries) {
            if (relationEntry.sameAs(entry)) {
                throw new RuntimeException(String.format("Dup Key Relation: existed %s, new entry %s",
                        relationEntry.toString(), entry.toString()));
            }
        }

        relationEntries.add(entry);

        // auto register key model if necessary: for left and right clazz
        // for the right clazz
        KeyModelEntry rightKeyModelEntry = model.getKeyModelReport().getOrCreateKeyModelEntryForActor(entry.getRightClass());
        if (rightKeyModelEntry.getPackageName() == null || rightKeyModelEntry.getPackageName().isEmpty()) {
            rightKeyModelEntry.setPackageName(entry.getRightClassPackageName());
        }
        // for the left clazz
        KeyModelEntry leftKeyModelEntry = model.getKeyModelReport().getOrCreateKeyModelEntryForActor(entry.getLeftClass());
        if (leftKeyModelEntry.getPackageName() == null || leftKeyModelEntry.getPackageName().isEmpty()) {
            leftKeyModelEntry.setPackageName(entry.getLeftClassPackageName());
            leftKeyModelEntry.setJavadoc(entry.getJavadoc());
        }
        return this;
    }

    public int size() {
        return relationEntries.size();
    }

}
