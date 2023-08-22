/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.report;

import io.github.dddplus.ast.model.KeyUsecaseEntry;
import lombok.Data;

import java.util.*;

@Data
public class KeyUsecaseReport {
    private Map<String, List<KeyUsecaseEntry>> data = new TreeMap<>();
    private Map<String, String> actorJavadoc = new HashMap<>();

    public void register(KeyUsecaseEntry entry) {
        if (!data.containsKey(entry.getClassName())) {
            data.put(entry.getClassName(), new ArrayList<>());
        }

        data.get(entry.getClassName()).add(entry);
    }

    public void registerActorJavadoc(String actor, String javadoc) {
        actorJavadoc.put(actor, javadoc);
    }

    public String actorJavadoc(String actor) {
        return actorJavadoc.get(actor);
    }

    public List<KeyUsecaseEntry> sortedActorKeyUsecases(String actor) {
        List<KeyUsecaseEntry> entries = data.get(actor);
        Collections.sort(entries, Comparator.comparing(KeyUsecaseEntry::getMethodName));
        return entries;
    }

    public int methods() {
        int n = 0;
        for (String actor : data.keySet()) {
            n += data.get(actor).size();
        }
        return n;
    }

}
