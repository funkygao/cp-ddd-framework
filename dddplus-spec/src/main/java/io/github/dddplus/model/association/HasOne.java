/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model.association;

/**
 * 关联对象，用于处理实体之间的生命周期边界.
 *
 * <p>生命周期边界，是指相关联的对象是否同时出现/消失在内存中.</p>
 * @param <Entity> 被关联对象类型
 */
public interface HasOne<Entity> {

    Entity get();
}
