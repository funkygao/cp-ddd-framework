package io.github.dddplus.ast.analysis;

import io.github.dddplus.ast.model.KeyModelEntry;

/**
 * 两个领域模型关键要素的相似度分析.
 */
public interface IDomainModelSimilarity {

    /**
     * 计算两个领域模型关键要素的相似度.
     *
     * @return in percentage
     */
    double similarity(KeyModelEntry model1, KeyModelEntry model2);
}
