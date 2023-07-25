/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.model;

import lombok.Builder;
import lombok.Data;

/**
 * Similarity between {@link KeyModelEntry}.
 */
@Data
@Builder
public class SimilarityEntry {
    private String leftClass;
    private String rightClass;
    private double similarity;
}
