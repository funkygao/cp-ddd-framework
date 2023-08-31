/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.report;

import io.github.dddplus.ast.model.CallGraphEntry;
import lombok.Data;

import java.util.*;

/**
 * call graph report.
 */
@Data
public class CallGraphReport {
    private List<CallGraphEntry> entries = new ArrayList<>();

    public List<CallGraphEntry> sortedEntries() {
        Collections.sort(entries, Comparator.comparing(c -> c.getCallerClazz() + c.getCallerMethod()));
        return entries;
    }

    public Collection<Record> calleeRecords() {
        List<Record> records = new ArrayList<>();
        Set<String> calleeClasses = new TreeSet<>();
        for (CallGraphEntry entry : entries) {
            calleeClasses.add(entry.getCalleeClazz());
        }
        for (String calleeClass : calleeClasses) {
            Record record = new Record(calleeClass);
            for (CallGraphEntry entry : entries) {
                if (!entry.getCalleeClazz().equals(calleeClass)) {
                    continue;
                }
                record.getMethods().add(entry.getCalleeMethod());
            }
            records.add(record);
        }
        return records;
    }

    public Collection<Record> callerRecords() {
        List<Record> records = new ArrayList<>();
        Set<String> callerClasses = new TreeSet<>();
        for (CallGraphEntry entry : entries) {
            callerClasses.add(entry.getCallerClazz());
        }
        for (String callerClazz : callerClasses) {
            Record record = new Record(callerClazz);
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

    public void register(CallGraphEntry entry) {
        entries.add(entry);
    }

    @Data
    public static class Record {
        private final String clazz;
        private Set<String> methods = new HashSet<>();

        Record(String clazz) {
            this.clazz = clazz;
        }

        public String dotNode() {
            return clazz.replaceAll("\\.", "_");
        }

        void addMethod(String method) {
            this.methods.add(method);
        }
    }
}
