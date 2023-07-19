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

    public Collection<Record> calleeRecords() {
        Map<String, Record> map = new HashMap<>();
        for (CallGraphEntry entry : entries) {
            final String calleeClazz = entry.getCalleeClazz();
            if (!map.containsKey(calleeClazz)) {
                map.put(entry.getCalleeClazz(), new Record(calleeClazz));
            }

            map.get(calleeClazz).addMethod(entry.getCalleeMethod());
        }

        return map.values();
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

        void addMethod(String method) {
            methods.add(method);
        }
    }
}
