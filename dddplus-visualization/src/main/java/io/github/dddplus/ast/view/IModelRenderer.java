package io.github.dddplus.ast.view;

import io.github.dddplus.ast.ReverseEngineeringModel;

import java.io.IOException;

interface IModelRenderer<T> {
    String SPACE = " ";
    String TAB = SPACE + SPACE + SPACE;
    String NEWLINE = System.getProperty("line.separator");

    <T> T build(ReverseEngineeringModel model);

    void render() throws IOException;
}
