/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model;

/**
 * 集合对象，Bag of some objects，处理内存中的集合逻辑.
 *
 * <p>封装集合的整体逻辑，例如：总数量，总重量，etc，还可以提供分组、过滤等算子生成新的{@link IBag}对象.</p>
 * <p>It is {@code portfolio} in 《Analysis Patterns》.</p>
 */
public interface IBag {
}
