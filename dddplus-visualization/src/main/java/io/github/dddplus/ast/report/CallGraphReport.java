/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.report;

import io.github.dddplus.ast.model.CallGraphEntry;
import io.github.dddplus.bce.CallGraphConfig;
import lombok.Data;
import lombok.NonNull;

import java.util.*;

/**
 * call graph report.
 */
@Data
public class CallGraphReport {
    private final CallGraphConfig config;
    private List<CallGraphEntry> entries = new ArrayList<>();

    public CallGraphReport(CallGraphConfig config) {
        this.config = config;
    }

    public List<CallGraphEntry> sortedEntries() {
        Collections.sort(entries, Comparator.comparing(c -> c.getCallerClazz() + c.getCallerMethod()));
        return entries;
    }

    public CallGraphReport preRender() {
        entries = effectiveEntries();
        return this;
    }

    private List<CallGraphEntry> effectiveEntries() {
        if (!config.ignoreOrphanNodes()) {
            return entries;
        }

        List<CallGraphEntry> result = new ArrayList<>(entries.size());
        for (int i = 0; i < entries.size(); i++) {
            CallGraphEntry entry = entries.get(i);
            if (!entry.isInnerClassCall()) {
                result.add(entry);
                continue;
            }

            for (int j = i + 1; j < entries.size(); j++) {
                CallGraphEntry that = entries.get(j);
                if (that.getCallerClazz().equals(entry.getCallerClazz()) && !that.isInnerClassCall()) {
                    result.add(entry);
                }
            }
        }

        return result;
    }

    private Collection<Record> calleeRecords() {
        List<Record> records = new ArrayList<>();
        Set<String> calleeClasses = new TreeSet<>();
        for (CallGraphEntry entry : entries) {
            calleeClasses.add(entry.getCalleeClazz());
        }
        for (String calleeClass : calleeClasses) {
            Record record = new Record(calleeClass, config);
            for (CallGraphEntry entry : entries) {
                if (!entry.getCalleeClazz().equals(calleeClass)) {
                    continue;
                }
                if (entry.isInvokeInterface()) {
                    record.invokeInterface = true;
                }
                record.getMethods().add(entry.getCalleeMethod());
            }
            records.add(record);
        }
        return records;
    }

    private Collection<Record> callerRecords() {
        List<Record> records = new ArrayList<>();
        Set<String> callerClasses = new TreeSet<>();
        for (CallGraphEntry entry : entries) {
            callerClasses.add(entry.getCallerClazz());
        }
        for (String callerClazz : callerClasses) {
            Record record = new Record(callerClazz, config);
            for (CallGraphEntry entry : entries) {
                if (!entry.getCallerClazz().equals(callerClazz)) {
                    continue;
                }

                record.addMethod(entry.getCallerMethod());
            }
            records.add(record);
        }
        return records;
    }

    public Map<String, Record> displayNodes() {
        // merged caller & callee by class name
        Map<String, CallGraphReport.Record> mergedNodes = new TreeMap<>();
        for (CallGraphReport.Record calleeClazz : calleeRecords()) {
            mergedNodes.put(calleeClazz.getClazz(), calleeClazz);
        }
        for (CallGraphReport.Record callerClazz : callerRecords()) {
            if (mergedNodes.containsKey(callerClazz.getClazz())) {
                mergedNodes.get(callerClazz.getClazz()).getMethods().addAll(callerClazz.getMethods());
            } else {
                mergedNodes.put(callerClazz.getClazz(), callerClazz);
            }
        }

        return mergedNodes;
    }

    public void register(CallGraphEntry entry) {
        entries.add(entry);
    }

    public boolean containsNode(@NonNull String clazzName) {
        for (Record node : callerRecords()) {
            if (node.sameAs(clazzName)) {
                return true;
            }
        }
        for (Record node : calleeRecords()) {
            if (node.sameAs(clazzName)) {
                return true;
            }
        }

        return false;
    }

    @Data
    public static class Record {
        private final CallGraphConfig config;
        private final String clazz;
        private Set<String> methods = new TreeSet<>();
        private boolean invokeInterface = false;

        Record(String clazz, CallGraphConfig config) {
            this.clazz = clazz;
            this.config = config;
        }

        boolean sameAs(String clazz) {
            if (!config.useSimpleClassName()) {
                return clazz.equals(this.clazz);
            }

            return this.clazz.contains(clazz);
        }

        public String dotNode() {
            if (config.useSimpleClassName()) {
                return clazz.substring(clazz.lastIndexOf(".") + 1);
            }

            return clazz.replaceAll("\\.", "_");
        }

        void addMethod(String method) {
            this.methods.add(method);
        }
    }
}
