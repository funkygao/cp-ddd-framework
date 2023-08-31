/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
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

    private String dotNode(String className) {
        return className.replaceAll("\\.", "_");
    }

    public String callerNode() {
        return dotNode(callerClazz) + ":" + callerMethod;
    }

    public String calleeNode() {
        return dotNode(calleeClazz) + ":" + calleeMethod;
    }
}
