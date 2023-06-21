/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.report;

import io.github.dddplus.ast.model.AggregateEntry;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AggregateReport {
    private List<AggregateEntry> aggregateEntries = new ArrayList<>();

    public int size() {
        return aggregateEntries.size();
    }

    public AggregateEntry get(int index) {
        return aggregateEntries.get(index);
    }

    public AggregateReport add(AggregateEntry aggregateEntry) {
        aggregateEntries.add(aggregateEntry);
        return this;
    }

    public AggregateEntry aggregateEntryOfPackage(String packageName) {
        for (AggregateEntry aggregateEntry : aggregateEntries) {
            if (aggregateEntry.belongToMe(packageName)) {
                return aggregateEntry;
            }
        }

        return null;
    }

}
