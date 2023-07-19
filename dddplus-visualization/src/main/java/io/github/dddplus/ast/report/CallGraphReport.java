package io.github.dddplus.ast.report;

import io.github.dddplus.ast.ReverseEngineeringModel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * call graph report.
 */
@Data
@Slf4j
public class CallGraphReport {
    private final ReverseEngineeringModel model;

    public CallGraphReport(ReverseEngineeringModel model) {
        this.model = model;
    }

    public boolean interestedInMethod(String methodName) {
        return model.getKeyModelReport().hasKeyMethod(methodName);
    }

    public void register(String callerClazz, String callerMethod, String calleeMethod) {
        log.info("{}.{} -> {}", callerClazz, callerMethod, calleeMethod);
    }
}
