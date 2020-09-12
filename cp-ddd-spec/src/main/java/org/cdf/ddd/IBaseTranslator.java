/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.cdf.ddd;

/**
 * DDD分层模型的类型转换，DTO -> Creator -> Entity <-> PO.
 * <p>
 * <p>使用{@code MapStruct} 或 {@code Selma}进行声明式类型转换</p>
 *
 * @param <Source> 源类型
 * @param <Target> 目标类型
 */
public interface IBaseTranslator<Source, Target> {

    /**
     * 映射同名属性，可以通过覆盖来实现更复杂的映射逻辑.
     * <p>
     * <p>包含常用的Java类型自动转换</p>
     *
     * @param source 源类型
     * @return 目标类型
     */
    Target translate(Source source);
}
