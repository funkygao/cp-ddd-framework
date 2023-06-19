/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.report;

import io.github.dddplus.ast.model.KeyRuleEntry;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Data
public class KeyRuleReport {
    // key is class name
    private Map<String, List<KeyRuleEntry>> keyRules = new TreeMap<>();

    public void register(KeyRuleEntry entry) {
        if (!keyRules.containsKey(entry.getClassName())) {
            keyRules.put(entry.getClassName(), new ArrayList<>());
        }

        keyRules.get(entry.getClassName()).add(entry);
    }

    public List<KeyRuleEntry> keyRulesOfClass(String className) {
        return keyRules.get(className);
    }

}
