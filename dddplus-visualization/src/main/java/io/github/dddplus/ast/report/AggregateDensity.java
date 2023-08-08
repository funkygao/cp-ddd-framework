/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.report;

import lombok.Data;

@Data
public class AggregateDensity {
    private int problems = 0;

    private double modelsMean;
    private double modelsStandardDeviation;

    private double methodDensityMean;
    private double methodDensityStandardDeviation;
}
