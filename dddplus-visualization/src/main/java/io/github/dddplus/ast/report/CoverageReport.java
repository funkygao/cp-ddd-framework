/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.report;

import lombok.Data;

/**
 * 逆向建模DSL覆盖率报告.
 */
@Data
public class CoverageReport {
    private int publicClazzN;
    private int annotatedClazzN;
    private int publicMethodN;
    private int annotatedMethodN;
    private int propertyN;
    private int annotatedPropertyN;

    public double propertyCoverage() {
        return annotatedPropertyN * 100. / propertyN;
    }

    public double methodCoverage() {
        return annotatedMethodN * 100. / publicMethodN;
    }

    public double clazzCoverage() {
        return annotatedClazzN * 100. / publicClazzN;
    }
}
