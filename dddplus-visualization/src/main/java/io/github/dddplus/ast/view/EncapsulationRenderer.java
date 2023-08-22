/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.view;

import io.github.dddplus.ast.model.ReverseEngineeringModel;
import io.github.dddplus.ast.parser.JavaParserUtil;
import io.github.dddplus.ast.report.EncapsulationReport;

import java.io.IOException;

/**
 * 分析public方法，以检查是否封装被泄露.
 */
public class EncapsulationRenderer implements IModelRenderer<EncapsulationRenderer> {
    private EncapsulationReport encapsulationReport;
    private String targetFilename;

    public EncapsulationRenderer targetFilename(String targetFilename) {
        this.targetFilename = targetFilename;
        return this;
    }

    @Override
    public EncapsulationRenderer withModel(ReverseEngineeringModel model) {
        encapsulationReport = model.getEncapsulationReport();
        return this;
    }

    @Override
    public void render() throws IOException {
        JavaParserUtil.dumpToFile(targetFilename, encapsulationReport.content());
    }
}
