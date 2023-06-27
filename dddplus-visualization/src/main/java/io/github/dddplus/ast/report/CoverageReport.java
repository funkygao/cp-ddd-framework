package io.github.dddplus.ast.report;

import lombok.Data;

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
