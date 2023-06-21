/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model;

/**
 * 领域对象，富含业务知识的高内聚业务对象，处理单对象逻辑.
 * <p>
 * <p>领域模型对象为限界上下文内受保护的纯内存对象，不能(序列化)将其暴露到外面，否则会造成外部对领域对象的耦合.</p>
 * <p>A domain model should be a strict and unambiguous representation of the domain that captures only the most important aspects.</p>
 * <p>业务规则不适于放在任何已有实体或值对象中，而且规则的变化和组合会掩盖那些领域对象的基本含义，可以放在{@code Specification}.</p>
 * <pre>
 * Order.PhoneNumber 开始时是必填的，但后来出现导入场景，可以为空，怎么办？规则是动态的，根据场景变化的.
 * {@link IDomainModel}本身对场景的表达力弱，如果校验规则都放在里面，就有问题：offload to Specification.
 * </pre>
 * <p>Order是Entity，这无疑问，但OrderLine是VO还是Entity？</p>
 * <p>这需要引入{@code Local Identity}概念，OrderLine拥有{@code Local Identity}，Order拥有{@code Global Identity}，它们都是Entity，都mutable.</p>
 */
public interface IDomainModel {
}
