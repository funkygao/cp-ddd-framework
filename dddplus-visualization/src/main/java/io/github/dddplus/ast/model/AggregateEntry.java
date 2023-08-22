/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AggregateEntry {

    /**
     * Name of the aggregate.
     */
    private String name;

    /**
     * 该聚合的root package name.
     */
    private String packageName;

    private boolean problematical;

    /**
     * {@link io.github.dddplus.model.IAggregateRoot} class name.
     */
    private String rootClass;
    private List<String> extraRootClasses = new ArrayList<>();

    private transient List<KeyModelEntry> keyModelEntries = new ArrayList<>();

    public void addKeyModelEntry(KeyModelEntry entry) {
        keyModelEntries.add(entry);
    }

    public void addExtraRootClass(String className) {
        extraRootClasses.add(className);
    }

    public List<KeyModelEntry> keyModels() {
        return keyModelEntries;
    }

    public boolean isRoot(KeyModelEntry entry) {
        return entry.getClassName().equals(rootClass);
    }

    /**
     * 指定的包名，是否属于本聚合.
     */
    public boolean belongToMe(String packageName) {
        return packageName.startsWith(this.packageName);
    }

    public boolean overlapWith(AggregateEntry that) {
        if (packageName.startsWith(that.packageName) || that.packageName.startsWith(packageName)) {
            return true;
        }

        return false;
    }

    public int modelsN() {
        return keyModelEntries.size();
    }

    public int methodDensity() {
        int n = 0;
        for (KeyModelEntry modelEntry : getKeyModelEntries()) {
            n += modelEntry.methodDensity();
        }
        return n;
    }
}
