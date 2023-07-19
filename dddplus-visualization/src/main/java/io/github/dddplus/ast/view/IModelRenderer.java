package io.github.dddplus.ast.view;

import io.github.dddplus.ast.ReverseEngineeringModel;

import java.io.IOException;

interface IModelRenderer<T> {

    <T> T build(ReverseEngineeringModel model);

    void render() throws IOException;
}
