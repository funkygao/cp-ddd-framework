package io.github.dddplus.ast.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CallGraphEntry {
    private String callerClazz;
    private String callerMethod;
    private String calleeClazz;
    private String calleeMethod;

    public String calleeNode() {
        return "\"" + calleeClazz + "." + calleeMethod + "\"";
    }
}
