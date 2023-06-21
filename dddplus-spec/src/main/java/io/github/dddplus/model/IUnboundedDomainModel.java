/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model;

/**
 * 非限定上下文下的领域对象.
 *
 * <p>承载自然属性，回答了"它是什么"这个问题，不易变，与具体场景无关，与运营无关.</p>
 * <p>场景特有的(数据，规则，行为)，请分配到{@link BoundedDomainModel}，这样才能做到"场景与领域分离"，避免聚合根膨胀模糊.</p>
 * <p>否则，会将“个性”统统视为“共性”.</p>
 * <p>一个{@link IUnboundedDomainModel}可能衍生出多个{@link BoundedDomainModel}</p>
 * <p>例如：一个人(unbounded)，在家是父亲(bounded)，在公司是员工(bounded)，在课堂上是老师(bounded)，不同角色，如果这些上下文逻辑都写到人这个类，必然膨胀臃肿，成为上帝类</p>
 * <p>应该这样设计：分别为(父亲，员工，老师)建模，建立边界清晰，职责单一，认知负荷低的模型；此外，不同上下文的演化路径、变更频率是不同的，分开后互不干扰，降低上线风险</p>
 * <ul>成为上帝类有什么问题？
 * <li>难修改：低内聚，违反单一职责，关注点不分离，不隔离变化，任意场景的变化都会引起这个类的变化，本该最稳定的却成了最不稳定元素</li>
 * <li>难理解：一个类中包含太多信息，主次不分，整体与局部不分，信息过载，让维护者难以理解</li>
 * <li>难测试：会导致难维护的单测代码</li>
 * <li>破坏模型：信息组织未结构化，场景知识丢失，被隐藏了</li>
 * </ul>
 * <p>领域和场景是不同的：领域是宏观层面，场景是微观层面.</p>
 * <p>在没有区分是否有界上下文对象前，聚合根里的行为其实可以理解为这样：</p>
 * <pre>
 * {@code
 *
 * class FooAggregateRoot implements ScenarioA, ScenarioB, ScenarioC, ScenarioD {
 *     void 与场景无关的通用方法() {}
 *     void scenarioAMethodXxx() {}
 *     void scenarioAMethodYyy() {}
 * }
 * }
 * </pre>
 * <p>由于Java语言的限制，我们无法拆分{@code class FooAggregateRoot}，造成它膨胀臃肿.</p>
 * <p>而通过区分是否有界上下文对象，我们做到了{@code class FooAggregateRoot}的拆分：不同Scenario的接口方法被分配到不同的{@link BoundedDomainModel}，从而避免了类臃肿.</p>
 */
public interface IUnboundedDomainModel extends IDomainModel {
}
