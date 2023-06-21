package io.github.dddplus.ast.analysis;

import io.github.dddplus.ast.analysis.IKeyModelSimilarity;
import io.github.dddplus.ast.model.KeyModelEntry;
import io.github.dddplus.dsl.KeyElement;

import java.util.HashSet;
import java.util.Set;

/**
 * Jaccard相似度(雅卡尔指数)用于计算两个集合的相似度，它是基于集合中的交集和并集计算的.
 */
public class JaccardModelSimilarity implements IKeyModelSimilarity {
    private final double StructuralWeight = 0.6;

    @Override
    public double similarity(KeyModelEntry model1, KeyModelEntry model2) {
        final KeyElement.Type typeA = KeyElement.Type.Structural;
        double s1 = jaccardSimilarity(model1.fieldNameSetByType(typeA), model2.fieldNameSetByType(typeA));
        final KeyElement.Type typeB = KeyElement.Type.Referential;
        double s2 = jaccardSimilarity(model1.fieldNameSetByType(typeB), model2.fieldNameSetByType(typeB));
        return (s1 * StructuralWeight + s2 * (1 - StructuralWeight)) * 100; // in percentage
    }

    private double jaccardSimilarity(Set<String> set1, Set<String> set2) {
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        int intersectionSize = intersection.size();
        int unionSize = set1.size() + set2.size() - intersectionSize;
        if (unionSize == 0) {
            // avoid NaN
            return 0;
        }

        return (double) intersectionSize / unionSize;
    }
}
