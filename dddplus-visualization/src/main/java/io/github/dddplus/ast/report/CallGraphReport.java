/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.report;

import io.github.dddplus.ast.model.PackageCrossRefEntry;
import io.github.dddplus.ast.model.ReverseEngineeringModel;
import io.github.dddplus.ast.model.CallGraphEntry;
import io.github.dddplus.ast.model.KeyModelEntry;
import lombok.Data;

import java.util.*;

/**
 * call graph report.
 */
@Data
public class CallGraphReport {
    private final ReverseEngineeringModel model;
    private List<CallGraphEntry> entries = new ArrayList<>();
    private Set<PackageCrossRefEntry> packageCrossRefEntries = new TreeSet<>();

    public CallGraphReport(ReverseEngineeringModel model) {
        this.model = model;
    }

    public boolean interestedInMethod(String declarationClazz, String methodName) {
        return model.getKeyModelReport().hasKeyMethod(declarationClazz, methodName);
    }

    public List<CallGraphEntry> sortedEntries() {
        Collections.sort(entries, Comparator.comparing(c -> c.getCallerClazz() + c.getCallerMethod()));
        return entries;
    }

    public boolean isKeyModel(String clazz) {
        return model.getKeyModelReport().containsActor(clazz);
    }

    private Set<String> calleeClasses() {
        Set<String> r = new TreeSet<>();
        for (CallGraphEntry entry : entries) {
            r.add(entry.getCalleeClazz());
        }
        return r;
    }

    public Collection<Record> calleeRecords() {
        List<Record> records = new ArrayList<>();
        for (String calleeClass : calleeClasses()) {
            Record record = new Record(calleeClass);
            record.addMethods(model.getKeyModelReport().keyModelEntryOfActor(calleeClass).realKeyMethods());

            records.add(record);
        }
        return records;
    }

    public void register(String callerClazz, String callerMethod, String calleeClazz, String calleeMethod) {
        if (!model.getKeyModelReport().containsActor(calleeClazz)) {
            // 被调用的方法不是我们标注的，例如 BigDecimal::add
            return;
        }

        KeyModelEntry calleeModel = model.getKeyModelReport().keyModelEntryOfActor(calleeClazz);
        if (!calleeModel.hasKeyMethod(calleeMethod)) {
            calleeModel.registerMethodFodCallGraph(calleeMethod);
        }
        KeyModelEntry callerModel = model.getKeyModelReport().keyModelEntryOfActor(callerClazz);
        if (callerModel != null && !callerModel.hasKeyMethod(callerMethod)) {
            callerModel.registerMethodFodCallGraph(callerMethod);
        }

        entries.add(new CallGraphEntry(callerClazz, callerMethod, calleeClazz, calleeMethod));
    }

    public void addPackageCrossRef(String callerPackage, String calleePackage) {
        if (callerPackage.equals(calleePackage)) {
            // only add cross packages relations
            return;
        }
        if (!model.hasPackage(calleePackage) || !model.hasPackage(callerPackage)) {
            return;
        }

        packageCrossRefEntries.add(new PackageCrossRefEntry(callerPackage, calleePackage));
    }

    @Data
    public static class Record {
        private final String clazz;
        private Set<String> methods = new HashSet<>();

        Record(String clazz) {
            this.clazz = clazz;
        }

        void addMethods(Set<String> methods) {
            this.methods.addAll(methods);
        }
    }
}
