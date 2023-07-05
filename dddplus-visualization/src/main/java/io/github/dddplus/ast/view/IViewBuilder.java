package io.github.dddplus.ast.view;

import io.github.dddplus.ast.ReverseEngineeringModel;

interface IViewBuilder<T> {

    <T> T build(ReverseEngineeringModel model);
}
