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
public class PackageCrossRefEntry implements Comparable<PackageCrossRefEntry> {
    private String callerPackage;
    private String calleePackage;

    @Override
    public int compareTo(PackageCrossRefEntry that) {
        return callerPackage.compareTo(that.callerPackage) * 71 + calleePackage.compareTo(that.calleePackage);
    }
}
