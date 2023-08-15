/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.model;

import io.github.dddplus.ast.algorithm.KMeans;
import io.github.dddplus.dsl.KeyElement;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
public class KeyModelEntry {
    private static final int propertiesPerLine = 5;

    private static final Set<String> EMPTY_SET = new HashSet();
    private static final Set<KeyPropertyEntry> EMPTY_PROPERTIES = new HashSet<>();

    @Setter
    private String packageName;
    private final String className;
    @Setter
    private String javadoc;
    // 这个类是个枚举
    @Setter
    private boolean enumType = false;

    private final Map<KeyElement.Type, List<KeyPropertyEntry>> properties;
    private final Set<String> rawFields = new HashSet<>();

    private transient List<KeyBehaviorEntry> keyBehaviorEntries = new ArrayList<>();
    private transient List<KeyRuleEntry> keyRuleEntries = new ArrayList<>();
    private transient List<KeyFlowEntry> keyFlowEntries = new ArrayList<>();

    // 这些方法本来没有通过DSL标注，但call graph时被调用，为了图的完整性临时增加这些节点
    // TODO 这里没有处理方法重载：在一个类中定义多个同名的方法，但要求每个方法具有不同的参数的类型或参数的个数
    private transient Set<String> methodsForCallGraph = new HashSet<>();

    public KeyModelEntry(String className) {
        this.className = className;
        this.properties = new TreeMap<>();
    }

    public List<KeyBehaviorEntry> sortedKeyBehaviorEntries() {
        Collections.sort(keyBehaviorEntries, Comparator.comparing(KeyBehaviorEntry::getMethodName));
        return keyBehaviorEntries;
    }

    public List<KeyRuleEntry> sortedKeyRuleEntries() {
        Collections.sort(keyRuleEntries, Comparator.comparing(KeyRuleEntry::getMethodName));
        return keyRuleEntries;
    }

    public List<KeyFlowEntry> sortedKeyFlowEntries() {
        Collections.sort(keyFlowEntries, Comparator.comparing(KeyFlowEntry::getSortedKey));
        return keyFlowEntries;
    }

    // TODO 这里没有处理方法重载
    public boolean hasKeyMethod(String methodName) {
        for (KeyBehaviorEntry entry : keyBehaviorEntries) {
            if (entry.getRealMethodName().equals(methodName)) {
                return true;
            }
        }
        for (KeyRuleEntry entry : keyRuleEntries) {
            if (entry.getRealMethodName().equals(methodName)) {
                return true;
            }
        }
        for (KeyFlowEntry entry : keyFlowEntries) {
            if (entry.getRealMethodName().equals(methodName)) {
                return true;
            }
        }
        return false;
    }

    public void registerMethodFodCallGraph(String methodName) {
        methodsForCallGraph.add(methodName);
    }

    // TODO 这里没有处理方法重载
    public Set<String> realKeyMethods() {
        Set<String> s = new TreeSet<>(methodsForCallGraph);
        for (KeyBehaviorEntry entry : keyBehaviorEntries) {
            s.add(entry.getMethodName());
        }
        for (KeyRuleEntry entry : keyRuleEntries) {
            s.add(entry.getMethodName());
        }
        for (KeyFlowEntry entry : keyFlowEntries) {
            /**
             * class FooService {
             *     @KeyFlow(actor = Order.class)
             *     void bar() {}
             * }
             *
             * 那么 Order里会记录该KeyFlowEntry，因此这里要排除
             */
            if (!entry.isOrphan()) {
                s.add(entry.getMethodName());
            }
        }
        return s;
    }

    public int methodDensity() {
        return keyBehaviorEntries.size() + keyRuleEntries.size() + keyFlowEntries.size();
    }

    public int propertiesN() {
        int n = 0;
        for (KeyElement.Type type : properties.keySet()) {
            n += properties.get(type).size();
        }
        return n;
    }

    public int problematicalPropertiesN() {
        if (!properties.containsKey(KeyElement.Type.Problematical)) {
            return 0;
        }
        return properties.get(KeyElement.Type.Problematical).size();
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

    public void addRawField(String fieldName) {
        rawFields.add(fieldName);
    }

    public Set<String> producedEvents() {
        Set<String> result = new TreeSet<>();
        for (KeyBehaviorEntry entry : keyBehaviorEntries) {
            if (entry.produceEvent()) {
                result.addAll(entry.getEvents());
            }
        }
        for (KeyFlowEntry entry : keyFlowEntries) {
            if (entry.produceEvent()) {
                result.addAll(entry.getEvents());
            }
        }

        return result;
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

        return new TreeSet<>(propertiesOfType);
    }

    public String displayFieldByType(KeyElement.Type type) {
        Set<KeyPropertyEntry> propertyEntries = keyPropertiesByType(type);
        List<String> fields = new ArrayList<>();
        int n = 0;
        for (KeyPropertyEntry entry : propertyEntries) {
            fields.add(entry.displayName(type));
            n++;
            if (n % propertiesPerLine == 0) {
                fields.add("\n");
            }
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

    public int orphanFlows() {
        int n = 0;
        for (KeyFlowEntry flowEntry : getKeyFlowEntries()) {
            if (flowEntry.isOrphan()) {
                n++;
            }
        }
        return n;
    }

    public List<List<String>> methodClusters() {
        if (keyBehaviorEntries.size() < 6) {
            // 太少，没必要聚类分析
            return null;
        }

        List<String> methodNames = new ArrayList<>(keyBehaviorEntries.size());
        for (KeyBehaviorEntry entry : keyBehaviorEntries) {
            methodNames.add(entry.getMethodName());
        }

        List<double[]> vectors = new ArrayList<>();
        Map<String, double[]> vectorMap = new HashMap<>();

        // 将方法名转换为向量
        int maxLength = -1;
        for (String methodName : methodNames) {
            if (methodName.length() > maxLength) {
                maxLength = methodName.length();
            }
        }
        for (String methodName : methodNames) {
            double[] vector = new double[maxLength];
            for (int i = 0; i < methodName.length(); i++) {
                vector[i] = methodName.charAt(i); // 向量值为ascii
            }
            // padding with 0
            for (int i = methodName.length(); i < maxLength; i++) {
                vector[i] = 0;
            }
            vectors.add(vector);
            vectorMap.put(methodName, vector);
        }

        // 使用K-Means算法进行聚类
        KMeans kMeans = new KMeans();
        return kMeans.cluster(vectors, methodNames, vectorMap);
    }
}
