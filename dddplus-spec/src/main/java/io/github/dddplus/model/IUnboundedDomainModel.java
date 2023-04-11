/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model;

/**
 * 非限定上下文下的领域对象.
 *
 * <p>与具体场景无关，与运营无关.</p>
 * <p>场景特有的(数据，规则，行为)，请分配到{@link BoundedDomainModel}，这样才能做到"场景与领域分离"，避免聚合根膨胀模糊，提炼知识形成富含知识的业务模型</p>
 * <p>一个{@link IUnboundedDomainModel}可能衍生出多个{@link BoundedDomainModel}</p>
 * <p>例如：一个人(unbounded)，在家是父亲(bounded)，在公司是员工(bounded)，在课堂上是老师(bounded)，不同角色，如果这些上下文逻辑都写到人这个类，必然膨胀臃肿</p>
 * <p>分别为(父亲，员工，老师)建模，建立边界清晰，职责单一，认知负荷低的模型；此外，不同上下文的演化路径、变更频率是不同的，分开后互不干扰，降低上线风险</p>
 */
public interface IUnboundedDomainModel extends IDomainModel {
}
