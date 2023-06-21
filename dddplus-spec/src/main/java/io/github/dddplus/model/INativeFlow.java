/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model;

/**
 * (有明确业务意图，跨聚合，可复用，不可拆分)的流程片段.
 *
 * <p>Unsplittable reusable single intention business orchestration logic.</p>
 * <p>它是一个(无状态，只暴露行为)的领域模型，富含领域知识，不属于某1个聚合.</p>
 * <p>Should I have domain knowledge to understand this code? Must be Yes.</p>
 * <p>对外是面向对象思维，内部是面向过程</p>
 * <p>
 * <ul>它与{@code Service}有什么区别？
 * <li>它只处理跨聚合根，不要仅仅因为有{@link IGateway}依赖而为聚合根创建{@link INativeFlow}，可以使用{@link BoundedDomainModel}</li>
 * <li>它只有1个公共方法，只有一个业务意图，只做一件事，单一职责</li>
 * <li>它强调复用性，前提是不可拆解，是微观的，因此不会用于`接单流程`这样复杂的宏流程：引起宏流程变化的原因有多种，而引起{@link INativeFlow}变化的原因只有一种</li>
 * <li>不必担心因此造成类数量膨胀，(有明确业务意图，跨聚合，可复用，不可拆分)的流程不可能多，否则就是聚合设计有问题</li>
 * <li>它的类名反映明确意图，而不会出现`OrderFlow`这样的无明确意图的命名，它不是无状态方法的容器</li>
 * <li>它的公共方法，可以统一命名为：execute，因为类名体现了意图，无需通过方法名体现</li>
 * </ul>
 * <p>聚合间如何协作？这里是通过{@link INativeFlow}，网上DDD说通过{@code domain events}，它有什么问题？</p>
 * <ul>
 * <li>EDA价值在于decouple producer from consumer，you generally don't have much concern for what the exact implementation is of the event consumers.</li>
 * <li>异步的代码不直观，阅读代码时(思维被中断，绕，跳，返程)，总是难以理解，除非异步机制写的的代码像同步一样</li>
 * <li>连续的生命周期 vs 离散的事件</li>
 * <li>事务的可理解性差</li>
 * <li>由于消费者被孤立到边缘，多方消费容易产生重复数据库查询、写，容易产生大事务</li>
 * <li>使得side effect隐性化</li>
 * <li>增加测试成本</li>
 * </ul>
 * @see <a href="https://learn.microsoft.com/en-us/dotnet/architecture/microservices/microservice-ddd-cqrs-patterns/domain-events-design-implementation">Single transaction across aggregates VS Eventual consistency across aggregates</a>
 */
public interface INativeFlow {
}
