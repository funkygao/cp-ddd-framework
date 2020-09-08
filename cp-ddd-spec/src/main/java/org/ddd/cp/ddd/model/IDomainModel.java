package org.ddd.cp.ddd.model;

/**
 * 领域模型，对应DDD的聚合根.
 * <p>
 * <p>世界由客体组成，主体认识客体的过程也是主体改造客体的过程</p>
 * <p>{@code IDomainModel}是客体，{@code IDomainService}是主体</p>
 * <p>应用程序的本质是认识世界（读），和改造世界（写）的过程</p>
 * <p>主体和客体在特定上下文下是可以互相转换的，收银员能操作订单，另一方面如果需要，收银员会被商户管理员作为人员操作</p>
 * <p>
 * <p>领域对象为限界上下文中受保护对象，绝对不应该将其暴露到外面</p>
 */
public interface IDomainModel {
}
