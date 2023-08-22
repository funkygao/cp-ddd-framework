/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.report;

import io.github.dddplus.ast.model.ReverseEngineeringModel;
import io.github.dddplus.ast.model.AggregateEntry;
import io.github.dddplus.ast.model.KeyModelEntry;
import lombok.Data;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.util.ArrayList;
import java.util.List;

/**
 * 聚合/模块的分析报告.
 */
@Data
public class AggregateReport {
    private final ReverseEngineeringModel model;
    private List<AggregateEntry> aggregateEntries = new ArrayList<>();

    public AggregateReport(ReverseEngineeringModel model) {
        this.model = model;
    }

    public AggregateEntry get(int index) {
        return aggregateEntries.get(index);
    }

    public AggregateReport add(AggregateEntry entry) {
        validate(entry);
        aggregateEntries.add(entry);

        // auto register key model
        for (String actor : entry.getExtraRootClasses()) {
            KeyModelEntry modelEntry = model.getKeyModelReport().getOrCreateKeyModelEntryForActor(actor);
            if (modelEntry.getPackageName() == null || modelEntry.getPackageName().isEmpty()) {
                modelEntry.setPackageName(entry.getPackageName());
            }
        }

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

    private void validate(AggregateEntry newEntry) {
        for (AggregateEntry entry : aggregateEntries) {
            if (entry.overlapWith(newEntry)) {
                throw new IllegalStateException(String.format("Aggregate overlaps (%s, %s)", entry.getName(), newEntry.getName()));
            }
        }
    }

    AggregateDensity density() {
        List<Integer> modelsOfAggregate = new ArrayList<>();
        List<Integer> methodDensity = new ArrayList<>();
        int problems = 0;
        for (AggregateEntry aggregateEntry : aggregateEntries) {
            modelsOfAggregate.add(aggregateEntry.modelsN());
            methodDensity.add(aggregateEntry.methodDensity());
            if (aggregateEntry.isProblematical()) {
                problems++;
            }
        }

        AggregateDensity density = new AggregateDensity();
        density.setProblems(problems);
        density.setModelsMean(new Mean().evaluate(toDoubleArray(modelsOfAggregate)));
        density.setModelsStandardDeviation(new StandardDeviation().evaluate(toDoubleArray(modelsOfAggregate)));
        density.setMethodDensityMean(new Mean().evaluate(toDoubleArray(methodDensity)));
        density.setMethodDensityStandardDeviation(new StandardDeviation().evaluate(toDoubleArray(methodDensity)));
        return density;
    }

    private double[] toDoubleArray(List<Integer> values) {
        double[] ds = new double[values.size()];
        for (int i = 0; i < ds.length; i++) {
            ds[i] = values.get(i);
        }
        return ds;
    }

}
