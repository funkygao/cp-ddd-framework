package io.github.dddplus.ast.report;

import io.github.dddplus.ast.ReverseEngineeringModel;
import io.github.dddplus.ast.model.CallGraphEntry;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * call graph report.
 */
@Data
@Slf4j
public class CallGraphReport {
    private final ReverseEngineeringModel model;
    private List<CallGraphEntry> entries = new ArrayList<>();

    public CallGraphReport(ReverseEngineeringModel model) {
        this.model = model;
    }

    public boolean interestedInMethod(String declarationClazz, String methodName) {
        return model.getKeyModelReport().hasKeyMethod(declarationClazz, methodName);
    }

    public void register(String callerClazz, String callerMethod, String calleeClazz, String calleeMethod) {
        entries.add(new CallGraphEntry(callerClazz, calleeMethod, calleeClazz, calleeMethod));
        log.info("{}.{} -> {}.{}", callerClazz, callerMethod, calleeClazz, calleeMethod);
    }

    public Set<String> nodes() {
        Set<String> result = new HashSet<>();
        for (CallGraphEntry entry : entries) {
            result.add(entry.calleeNode());
            result.add(entry.callerNode());
        }
        return result;
    }
}
