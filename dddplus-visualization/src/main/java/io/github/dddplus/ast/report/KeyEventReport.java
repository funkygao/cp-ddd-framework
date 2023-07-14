/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.report;

import io.github.dddplus.ast.model.KeyEventEntry;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Data
public class KeyEventReport {
    private List<KeyEventEntry> events = new ArrayList<>();

    public KeyEventReport register(KeyEventEntry entry) {
        events.add(entry);
        return this;
    }

    public List<KeyEventEntry> sortedEvents() {
        Collections.sort(events, Comparator.comparing(KeyEventEntry::getClassName));
        return events;
    }

    public int size() {
        return events.size();
    }

    public boolean isEmpty() {
        return events.isEmpty();
    }

}
