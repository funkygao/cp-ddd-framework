/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model;

import io.github.dddplus.ext.IIdentity;

/**
 * 聚合根，Aggregate Facade，(具有全局身份，负责维护聚合内一致性).
 *
 * <p>Aggregate is a tree with a single root：the {@link IAggregateRoot}，聚合内其他{@code Entity}都是Local Entity，Local Entity不能脱离Global Entity存在和被引用.</p>
 * <p>聚合的本质就是建立了一个比类粒度更大的边界，聚集那些紧密关联的对象，形成了一个业务整体(边界).</p>
 * <p>这样，外部需要关心的模型元素数量减少，复杂性下降：避免了错综复杂难以维护的对象关系网的形成.</p>
 * <p>寻找对象关系天然就比较少的地方作为聚合的边界划分!</p>
 * <p>聚合边界外有side effect的交互都必须通过{@link IAggregateRoot}，只有这样才能保证业务一致性，副作用局部化，satisfy invariants，状态变化才可跟踪.</p>
 * <ol>聚合根，作为聚合的门面，如何把住大门？记住这2个原则：
 * <li>The root entity controls access and cannot be blindsided by changes to its internals</li>
 * <li>passing out references to internal members that support MUTATION operations is NOT Allowed</li>
 * </ol>
 * <ol>设计聚合边界内的对象应满足如下一致性规则：
 * <li>生命周期一致性</li>
 * <li>问题域一致性</li>
 * <li>场景一致性</li>
 * </ol>
 */
public interface IAggregateRoot extends IIdentity, IDomainModel {
}
