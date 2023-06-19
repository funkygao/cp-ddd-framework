/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.report;

import io.github.dddplus.ast.model.KeyModelEntry;
import lombok.Data;

import java.util.*;

@Data
public class KeyModelReport {
    private Map<String, KeyModelEntry> data = new TreeMap<>();

    public KeyModelEntry getOrCreateKeyModelEntryForActor(String actor) {
        if (!data.containsKey(actor)) {
            data.put(actor, new KeyModelEntry(actor));
        }

        return data.get(actor);
    }

    public int models() {
        return data.size();
    }

    public int methods() {
        int n = 0;
        for (KeyModelEntry entry : data.values()) {
            n += entry.methods();
        }
        return n;
    }

    public boolean containsActor(String actor) {
        return data.containsKey(actor);
    }

    public KeyModelEntry keyModelEntryOfActor(String actor) {
        return data.get(actor);
    }

    public Set<String> actors() {
        return data.keySet();
    }

    public List<KeyModelEntry> keyModelsOfPackage(String packageName) {
        List<KeyModelEntry> result = new ArrayList<>();
        for (KeyModelEntry entry : data.values()) {
            if (entry.getPackageName().startsWith(packageName)) {
                result.add(entry);
            }
        }

        return result;
    }

}
