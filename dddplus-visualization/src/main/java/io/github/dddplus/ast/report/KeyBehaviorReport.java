/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.report;

import io.github.dddplus.ast.model.KeyBehaviorEntry;
import lombok.Data;

import java.util.*;

@Data
public class KeyBehaviorReport {
    private Map<String, List<KeyBehaviorEntry>> data = new TreeMap<>();

    public void register(KeyBehaviorEntry entry) {
        if (!data.containsKey(entry.getClassName())) {
            data.put(entry.getClassName(), new ArrayList<>());
        }

        data.get(entry.getClassName()).add(entry);
    }

    public List<KeyBehaviorEntry> actorKeyBehaviors(String actor) {
        return data.get(actor);
    }

    public Set<String> actors() {
        return data.keySet();
    }

}
