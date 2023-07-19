package io.github.dddplus.ast.report;

import lombok.Data;

@Data
public class AggregateDensity {
    private double modelsMean;
    private double modelsStandardDeviation;

    private double methodDensityMean;
    private double methodDensityStandardDeviation;
}
