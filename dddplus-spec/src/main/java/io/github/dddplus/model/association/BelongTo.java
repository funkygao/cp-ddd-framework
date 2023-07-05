/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model.association;

/**
 * 关联对象：隶属于.
 *
 * <p>Example:</p>
 * <pre>
 * {@code
 *
 * class Carton {
 *     // 一个周转箱隶属于一个复核任务
 *     OwnerTask ownerTask;
 *     public OwnerTask ownerTask() {
 *         return ownerTask;
 *     }
 *
 *     public interface OwnerTask extends BelongTo<Task> {
 *     }
 * }
 *
 * Carton carton = repo.get(cartonNo);
 * // ownerTask()，相当于指针；引用关系，通过关联对象被连接s
 * CheckTask task = carton.ownerTask().get();
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
