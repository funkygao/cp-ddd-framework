package io.github.dddplus.ast.model.dumper;

import io.github.dddplus.ast.model.ReverseEngineeringModel;

public interface ModelDumper {
    void dump(ReverseEngineeringModel model) throws Exception;
}
