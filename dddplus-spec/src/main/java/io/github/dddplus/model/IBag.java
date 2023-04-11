/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model;

/**
 * Bag of some objects.
 *
 * <p>用于标记领域模型里的集合对象.</p>
 * <p>集合对象收敛集合的整体逻辑，例如：总数量，总重量，etc，还可以提供分组、过滤等算子生成新的{@link IBag}对象.</p>
 */
public interface IBag {
}
