/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model.association;

/**
 * 关联对象 隶属于，用于处理实体之间的生命周期边界.
 *
 * <p>生命周期边界，是指相关联的对象是否同时出现/消失在内存中.</p>
 * <p>Example:</p>
 * <pre>
 * {@code
 *
 * class Carton {
 *     // 一个周转箱隶属于一个复核任务
 *     BelongToCheckTask ownerTask;
 *
 *     public interface BelongToCheckTask extends BelongTo<CheckTask> {
 *     }
 * }
 *
 * Carton carton = repo.get(cartonNo);
 * CheckTask task = carton.getOwnerTask().get();
 * }
 * </pre>
 * @param <Entity> 被关联对象类型
 */
public interface BelongTo<Entity> {

    /**
     * 获取隶属的关联对象.
     */
    Entity get();
}
