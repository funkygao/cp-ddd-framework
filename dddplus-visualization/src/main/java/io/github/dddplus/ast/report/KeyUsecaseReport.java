/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.report;

import io.github.dddplus.ast.model.KeyUsecaseEntry;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Data
public class KeyUsecaseReport {
    private Map<String, List<KeyUsecaseEntry>> data = new TreeMap<>();

    public void register(KeyUsecaseEntry entry) {
        if (!data.containsKey(entry.getClassName())) {
            data.put(entry.getClassName(), new ArrayList<>());
        }

        data.get(entry.getClassName()).add(entry);
    }

    public List<KeyUsecaseEntry> actorKeyUsecases(String actor) {
        return data.get(actor);
    }

    public int methods() {
        int n = 0;
        for (String actor : data.keySet()) {
            n += data.get(actor).size();
        }
        return n;
    }

}
