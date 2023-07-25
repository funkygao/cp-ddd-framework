/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.report;

import io.github.dddplus.ast.model.ReverseEngineeringModel;
import io.github.dddplus.ast.model.KeyFlowEntry;
import lombok.Data;

import java.util.*;

@Data
public class KeyFlowReport {
    private final ReverseEngineeringModel model;

    // key is actor
    private Map<String, List<KeyFlowEntry>> keyFlows = new TreeMap<>();

    public KeyFlowReport(ReverseEngineeringModel model) {
        this.model = model;
    }

    public void register(KeyFlowEntry entry) {
        final String actor = entry.actor();
        if (!keyFlows.containsKey(actor)) {
            keyFlows.put(actor, new ArrayList<>());
        }

        keyFlows.get(actor).add(entry);
    }

    public Set<String> actors() {
        return keyFlows.keySet();
    }

    public boolean hasOrphanFlowEntries() {
        if (actors().isEmpty()) {
            return false;
        }

        for (String actor : actors()) {
            List<KeyFlowEntry> orphanFlowsOfActor = orphanFlowsOfActor(actor);
            if (!orphanFlowsOfActor.isEmpty()) {
                if (orphanFlowsOfActor.stream().allMatch(KeyFlowEntry::isOrphan)) {
                    return true;
                }
            }
        }

        return false;
    }

    public List<KeyFlowEntry> keyFlowEntriesOfActor(String actor) {
        return keyFlows.get(actor);
    }

    public List<KeyFlowEntry> orphanFlowsOfActor(String actor) {
        List<KeyFlowEntry> result = new ArrayList<>();
        for (KeyFlowEntry entry : keyFlowEntriesOfActor(actor)) {
            if (entry.isOrphan()) {
                result.add(entry);
            }
        }
        return result;
    }

    public int orphanMethods() {
        int n = 0;
        for (String actor : keyFlows.keySet()) {
            n += keyFlows.get(actor).size();
        }
        return n;
    }

}
