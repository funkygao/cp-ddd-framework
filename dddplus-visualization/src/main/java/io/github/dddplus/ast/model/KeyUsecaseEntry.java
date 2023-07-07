/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.model;

import lombok.Data;

import java.util.List;

@Data
public class KeyUsecaseEntry {
    private String className;
    private String methodName;
    private String realMethodName;
    private String remark = "";
    private List<String> in;
    private List<String> out;
    private String javadoc;
    private String keyEvent;

    public KeyUsecaseEntry(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
        this.realMethodName = methodName;
    }

    public String displayNameWithRemark() {
        if (remark == null || remark.isEmpty()) {
            return methodName;
        }

        return methodName + "/" + remark;
    }

    public boolean hasJavadoc() {
        return javadoc != null && !javadoc.isEmpty();
    }

    public boolean isConsumer() {
        return keyEvent != null && !keyEvent.isEmpty();
    }

    public String displayIn() {
        if (in == null || in.isEmpty()) {
            return "";
        }

        return String.join(",", in);
    }

    public String displayOut() {
        if (out == null || out.isEmpty()) {
            return "";
        }

        return String.join(",", out);
    }
}
