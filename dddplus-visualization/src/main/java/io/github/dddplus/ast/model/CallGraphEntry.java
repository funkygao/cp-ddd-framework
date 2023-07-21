/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.model;

import io.github.dddplus.ast.report.CallGraphReport;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CallGraphEntry {
    private String callerClazz;
    private String callerMethod;
    private String calleeClazz;
    private String calleeMethod;

    public String callerNode(CallGraphReport report) {
        if (report.isKeyModel(callerClazz)) {
            return callerClazz + ":" + callerMethod;
        }

        return callerClazz;

    }

    public String calleeNode() {
        return calleeClazz + ":" + calleeMethod;
    }
}
