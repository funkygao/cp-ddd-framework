package io.github.dddplus.ast.report;

import io.github.dddplus.ast.ReverseEngineeringModel;
import io.github.dddplus.ast.model.CallGraphEntry;
import lombok.Data;

import java.util.*;

/**
 * call graph report.
 */
@Data
public class CallGraphReport {
    private final ReverseEngineeringModel model;
    private List<CallGraphEntry> entries = new ArrayList<>();

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
        entries.add(new CallGraphEntry(callerClazz, callerMethod, calleeClazz, calleeMethod));
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
