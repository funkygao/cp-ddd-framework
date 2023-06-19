/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model;

/**
 * 领域层服务.
 * <p>
 * <p>领域中的一些概念不太适合建模为对象，因为它们本质上就是一些操作，一些动作，而不是事物.</p>
 * <p>{@link IDomainService}是实现上的一种妥协，不该成为设计的核心：在Entity/VO等无法满足时才用它，理想目标是0个{@link IDomainService}.</p>
 * <p>{@link IDomainService}只负责业务规则，不负责业务流程，业务流程由{@link IApplicationService}负责.</p>
 * <ul>职责上{@link IApplicationService} vs {@link IDomainService}
 * <li>前者是横向层，协调/驱动不同的领域对象和领域服务，实现业务流程的调度和控制，处理与用户交互的请求，以及与领域模型之间的交互</li>
 * <li>是面向用户和系统的外部接口，此外，还负责事务的管理、权限的控制、数据校验等</li>
 * <li>后者是纵向层，负责聚合之间的协作和交互，实现领域模型中的业务逻辑，封装领域模型中复杂的业务流程和算法，实现领域模型的高内聚、低耦合和可复用性</li>
 * <li>是面向领域模型的内部接口，负责对领域模型进行拓展、补充和优化</li>
 * <li>横向做编排，纵向做隔离</li>
 * <li>简单来说，前者负责应用程序的控制流程和与外部系统的交互，后者负责领域内的业务逻辑和领域模型的拓展和优化</li>
 * </ul>
 */
public interface IDomainService {
}
