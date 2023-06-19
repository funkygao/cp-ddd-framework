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
