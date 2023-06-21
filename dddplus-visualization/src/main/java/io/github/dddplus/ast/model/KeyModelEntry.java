/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.model;

import io.github.dddplus.dsl.KeyElement;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
public class KeyModelEntry {
    private static final Set<String> EMPTY_SET = new HashSet();
    private static final Set<KeyPropertyEntry> EMPTY_PROPERTIES = new HashSet<>();

    @Setter
    private String packageName;
    private final String className;
    @Setter
    private String javadoc;
    private final Map<KeyElement.Type, List<KeyPropertyEntry>> properties;

    private transient List<KeyBehaviorEntry> keyBehaviorEntries = new ArrayList<>();
    private transient List<KeyRuleEntry> keyRuleEntries = new ArrayList<>();
    private transient List<KeyFlowEntry> keyFlowEntries = new ArrayList<>();

    public KeyModelEntry(String className) {
        this.className = className;
        this.properties = new TreeMap<>();
    }

    public int methods() {
        return keyBehaviorEntries.size() + keyRuleEntries.size() + keyFlowEntries.size();
    }

    public boolean hasJavadoc() {
        return javadoc != null && !javadoc.trim().isEmpty();
    }

    public boolean isBehaviorOnly() {
        return properties.isEmpty();
    }

    public Set<KeyElement.Type> types() {
        return properties.keySet();
    }

    public KeyModelEntry addField(KeyElement.Type type, KeyPropertyEntry keyPropertyEntry) {
        if (!properties.containsKey(type)) {
            properties.put(type, new ArrayList<>());
        }

        properties.get(type).add(keyPropertyEntry);
        return this;
    }

    public List<KeyElement.Type> undefinedTypes() {
        List<KeyElement.Type> result = new ArrayList<>();
        for (KeyElement.Type type : KeyElement.Type.values()) {
            if (type.compareTo(KeyElement.Type.Problematical) >= 0) {
                // ignored
                continue;
            }

            if (!properties.containsKey(type)) {
                result.add(type);
            }
        }

        return result;
    }

    public Set<String> fieldNameSetByType(KeyElement.Type type) {
        Set<KeyPropertyEntry> propertiesOfType = keyPropertiesByType(type);
        if (propertiesOfType.isEmpty()) {
            return EMPTY_SET;
        }

        Set<String> result = new HashSet<>();
        for (KeyPropertyEntry propertyEntry : propertiesOfType) {
            result.add(propertyEntry.getName());
        }

        return result;
    }

    public Set<KeyPropertyEntry> keyPropertiesByType(KeyElement.Type type) {
        List<KeyPropertyEntry> propertiesOfType = properties.get(type);
        if (propertiesOfType == null) {
            return EMPTY_PROPERTIES;
        }

        return new HashSet<>(propertiesOfType);
    }

    public String displayUndefinedTypes() {
        List<KeyElement.Type> types = undefinedTypes();
        if (types.isEmpty()) {
            return "";
        }

        StringJoiner joiner = new StringJoiner(" ");
        for (KeyElement.Type t: types) {
            joiner.add(t.toString());
        }
        return joiner.toString();
    }

    public String displayFieldByType(KeyElement.Type type) {
        Set<KeyPropertyEntry> propertyEntries = keyPropertiesByType(type);
        Set<String> fields = new TreeSet<>();
        for (KeyPropertyEntry entry : propertyEntries) {
            fields.add(entry.displayName());
        }

        return String.join(" ", fields);
    }

    public void addKeyBehaviorEntries(List<KeyBehaviorEntry> entries) {
        if (entries == null || entries.isEmpty()) {
            return;
        }

        // sort by method name
        Collections.sort(entries, Comparator.comparing(KeyBehaviorEntry::getMethodName));
        this.keyBehaviorEntries.addAll(entries);
    }

    public void addKeyRuleEntries(List<KeyRuleEntry> entries) {
        if (entries == null || entries.isEmpty()) {
            return;
        }

        this.keyRuleEntries.addAll(entries);
    }

    public void addKeyFlowEntries(List<KeyFlowEntry> entries) {
        if (entries == null || entries.isEmpty()) {
            return;
        }

        this.keyFlowEntries.addAll(entries);
    }
}
