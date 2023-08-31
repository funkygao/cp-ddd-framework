/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.report;

import io.github.dddplus.ast.model.ReverseEngineeringModel;
import io.github.dddplus.ast.model.KeyEventEntry;
import io.github.dddplus.ast.model.KeyModelEntry;
import lombok.Data;

import java.util.*;

@Data
public class KeyModelReport {
    private final ReverseEngineeringModel model;

    private Map<String, KeyModelEntry> data = new TreeMap<>();
    private Map<String, KeyModelEntry> rawModels = new TreeMap<>();

    public KeyModelReport(ReverseEngineeringModel model) {
        this.model = model;
    }

    /**
     * 使用注意：返回的{@link KeyModelEntry#getPackageName()}是空的，需要接下来赋值，否则出报告时NPE.
     */
    public KeyModelEntry getOrCreateRawModelEntry(String className) {
        if (!rawModels.containsKey(className)) {
            rawModels.put(className, new KeyModelEntry(className));
        }

        return rawModels.get(className);
    }

    public void fixPackage(String fromPkg, String toPkg) {
        if (!fromPkg.contains(".")) {
            // fix a single entity pkg
            String keyModelName = fromPkg;
            KeyModelEntry entry = data.get(keyModelName);
            if (entry != null) {
                entry.setPackageName(toPkg);
            }
            return;
        }

        for (KeyModelEntry entry : data.values()) {
            if (fromPkg.equals(entry.getPackageName())) {
                entry.setPackageName(toPkg);
            }
        }
    }

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

    public int propertiesN() {
        int n = 0;
        for (KeyModelEntry entry : data.values()) {
            n += entry.propertiesN();
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
            if (entry.getPackageName() != null && entry.getPackageName().startsWith(packageName)) {
                result.add(entry);
            }
        }

        return result;
    }

    public boolean hasProducer(KeyEventEntry event) {
        for (KeyModelEntry entry : data.values()) {
            if (entry.producedEvents().contains(event.getClassName())) {
                return true;
            }
        }

        return false;
    }

}
