/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model;

/**
 * 关联对象.
 *
 * <p>使用时赋予其具体业务语义，并提供关联对象的实现.</p>
 * <p>Example:</p>
 * <pre>
 * {@code
 * public class Task {
 *     private Orders orders;
 *
 *     public interface Orders extends HasMany<Order> {
 *         List<Order> pendingOrders();
 *     }
 * }
 *
 * public TaskOrders implements Task.Orders {
 *     private final Task task;
 *     public TaskOrders(Task task) {
 *         this.task = task;
 *     }
 *
 *     public List<Order> pendingOrders() {
 *         return dao.findPendingOrdersByTask(task.getTaskNo());
 *     }
 * }
 * }
 * </pre>
 *
 * @param <Entity> 被关联对象类型
 */
public interface HasMany<Entity> extends IBag {
}
