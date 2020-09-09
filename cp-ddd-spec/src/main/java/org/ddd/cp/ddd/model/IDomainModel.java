package org.ddd.cp.ddd.model;

/**
 * 领域模型，对应DDD的聚合根.
 * <p>
 * <p>世界由客体组成，主体认识客体的过程也是主体改造客体的过程</p>
 * <p>{@code IDomainModel}是客体，{@code IDomainService}是主体</p>
 * <p>应用程序的本质是认识世界（读），和改造世界（写）的过程</p>
 * <p>
 * <p>领域对象为限界上下文(BC)中受保护对象，绝对不应该将其暴露到外面！</p>
 */
public interface IDomainModel {
}
