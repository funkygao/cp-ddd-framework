/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.view;

import io.github.dddplus.ast.model.ReverseEngineeringModel;

import java.io.IOException;

interface IModelRenderer<T extends IModelRenderer> {
    String SPACE = " ";
    String TAB = SPACE + SPACE + SPACE;
    String NEWLINE = System.getProperty("line.separator");

    T withModel(ReverseEngineeringModel model);

    void render() throws IOException;
}
