package io.github.dddplus.ast.view;

import io.github.dddplus.ast.ReverseEngineeringModel;

/**
 * DSL -> Reverse Engineering Model -> D3.js.
 */
public class D3jsBuilder implements IViewBuilder<D3jsBuilder> {
    private final StringBuilder content = new StringBuilder();
    private ReverseEngineeringModel model;

    @Override
    public D3jsBuilder build(ReverseEngineeringModel model) {
        this.model = model;
        return this;
    }

    public void render() {

    }
}
