/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.model.dumper;

import io.github.dddplus.ast.model.ReverseEngineeringModel;

/**
 * {@link ReverseEngineeringModel} dumper.
 */
public interface ModelDumper {
    void dump(ReverseEngineeringModel model) throws Exception;
}
