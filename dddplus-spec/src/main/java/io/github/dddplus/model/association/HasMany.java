/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model.association;

import io.github.dddplus.model.IAggregateRoot;
import io.github.dddplus.model.IBag;

/**
 * 关联对象：1对多.
 *
 * <p>Learned from 《Analysis Patterns》14.1.2 Interface for Associations：The interface for associations in an OO language is a series of operations to access and update the association.</p>
 * <p>关联对象用于处理实体之间的生命周期边界，生命周期边界是指相关联的对象是否同时出现/消失在内存中.</p>
 * <p>对象关系，有2种：(聚合关系，引用关系)，前者通过{@link IAggregateRoot}实现；后者通过关联对象实现，从而避免逻辑泄露.</p>
 * <p>设计上是把关联关系设计成对象(接口).</p>
 * <p>有了关联对象，就去掉了与(数据库，外部服务)等外部依赖的强关联.</p>
 * <pre>
 *      the association object: interface/pointer
 *             ^
 *             │
 *     user.orders().list()
 *                     │
 *                     V
 *           load into memory from somewhere(db/remote api/etc)
 * </pre>
 * <p>Example:</p>
 * <pre>
 * {@code
 *
 * ℗lombok.Getter(AccessLevel.PACKAGE)
 * public class Task implements IAggregateRoot {
 *     public interface Orders extends HasMany<Order> {
 *         List<Order> pendingOrders();
 *     }
 *
 *     ℗lombok.experimental.Delegate
 *     private Orders orders; // task.pendingOrders()
 * }
 * public class Order implements IAggregateRoot {}
 *
 * // 在infrastructure层实现
 * public TaskOrders implements Task.Orders {
 *     private final String taskNo;
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
