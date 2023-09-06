/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.model;

import io.github.dddplus.bce.CallGraphConfig;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CallGraphEntry {
    private final CallGraphConfig config;
    private final String callerClazz;
    private final String callerMethod;
    private final String calleeClazz;
    private final String calleeMethod;

    private boolean invokeInterface = false;
    private boolean invokeStatic = false;

    public boolean isInnerClassCall() {
        return calleeClazz.equals(callerClazz);
    }

    private String dotNode(String fullName) {
        if (config.useSimpleClassName()) {
            return fullName.substring(fullName.lastIndexOf(".") + 1);
        }

        // dot语法不支持'.'作为node
        return fullName.replaceAll("\\.", "_");
    }

    public String callerNode() {
        return dotNode(callerClazz) + ":" + callerMethod;
    }

    public String calleeNode() {
        return dotNode(calleeClazz) + ":" + calleeMethod;
    }

}
