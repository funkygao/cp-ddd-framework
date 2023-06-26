/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model.association;

import io.github.dddplus.model.IBag;

/**
 * 关联对象 1对多，用于处理实体之间的生命周期边界.
 *
 * <p>生命周期边界，是指相关联的对象是否同时出现/消失在内存中.</p>
 * <p>使用时赋予其具体业务语义，并提供关联对象的实现.</p>
 * <p>Example:</p>
 * <pre>
 * {@code
 *
 * public class Task implements IAggregateRoot {
 *     private Orders orders;
 *
 *     public interface Orders extends HasMany<Order> {
 *         List<Order> pendingOrders();
 *     }
 * }
 * public class Order implements IAggregateRoot {}
 *
 * // 在infrastructure层实现
 * public TaskOrders implements Task.Orders {
 *     private final String taskNo;
 *     // autowired by AutowireObjectFactory in dddplus-mybatis module
 *     private Dao dao;
 *     public TaskOrders(String taskNo) {
 *         this.taskNo = taskNo;
 *     }
 *
 *     public List<Order> pendingOrders() {
 *         return dao.findPendingOrdersByTask(taskNo);
 *     }
 * }
 *
 * // mybatis/mapper.xml
 * <resultMap id="taskOrders" type="io.github.design.mybatis.associations.TaskOrders">
 *     <result column="task_no" property="taskNo" javaType="String"/>
 * </resultMap>
 * <resultMap id="checkTask" type="io.github.design.CheckTask">
 *     <id column="id" property="id" jdbcType="BIGINT"/>
 *     <association property="orders" resultMap="taskOrders"/>
 * </resultMap>
 * }
 * </pre>
 *
 * @param <Entity> 被关联对象类型
 */
public interface HasMany<Entity> extends IBag {
}
