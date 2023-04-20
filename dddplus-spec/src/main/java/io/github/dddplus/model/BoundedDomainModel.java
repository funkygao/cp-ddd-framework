/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model;

import io.github.dddplus.ext.IDomainExtension;

/**
 * 限定上下文的领域模型，又称角色对象(Methodful Role)：显式对Role建模(基于Role Method).
 *
 * <p>具体场景相关，运营相关，承载对应场景下的(数据，规则，行为)，不能脱离{@link IUnboundedDomainModel}独立存在，高内聚：它的代码改动只来源于该场景上下文需求</p>
 * <p>{@link IUnboundedDomainModel}通过扮演某个角色进入某个特定场景时拥有的属性或行为，成为{@link BoundedDomainModel}</p>
 * <p>{@link IUnboundedDomainModel}回答了"它是什么"这个问题，不易变；{@link BoundedDomainModel}回答了"它做了什么"这个问题，易变，聚焦于场景特有行为</p>
 * <ul>使用规范：
 * <li>一个{@link IUnboundedDomainModel}的多个{@link BoundedDomainModel}间要满足正交性，不能有重叠：不能角色错位</li>
 * <li>一次交互可以涉及多角色对象吗？可以，例如下面示例代码里，Buyer把购买到的商品赠送给朋友Contact</li>
 * <li>{@code if xxx order.asXxx() else if yyy order.asYyy()}，这样使用角色对象允许吗？不允许</li>
 * <li>两个角色对象有相同的行为，只是内部实现不同，可以吗？不可以，使用{@link IDomainExtension}解决多态问题。区别不同角色的是Role Method(扮演各自角色时，只能做出符合自己角色身份的行为)，而不是同一个Method的不同实现(怎么做)</li>
 * <li>是什么 vs 做什么 vs 做不做 vs 怎么做</li>
 * </ul>
 * <pre>
 * {@code
 * class User implements IUnboundedDomainModel {
 *     Buyer asBuyer() {
 *         return new Buyer(this);
 *     }
 *     Contact asContact() {
 *         return new Contact(this);
 *     }
 *     Debtor asDebtor() {
 *         return new Debtor(this);
 *     }
 * }
 *
 * // 电商上下文，用户角色是Buyer
 * class Buyer extends BoundedDomainModel<User> {
 *     void placeOrder() {}
 * }
 *
 * // 社交上下文，用户角色是Contact
 * class Contact extends BoundedDomainModel<User> {
 *     List<Friend> myFriends() {}
 *     void makeFriend(Contact whom) {}
 * }
 *
 * // 金融上下文，用户角色是债务人
 * class Debtor extends BoundedDomainModel<User> {
 *     void loan(Money amount) {}
 *     void repay() {}
 * }
 *
 * interface UserRepository {
 *     User get(Long userId);
 *     Buyer asBuyer(User user);
 *     Contact asContact(User user);
 * }
 * }
 * </pre>
 * @see <a href="https://martinfowler.com/bliki/RoleInterface.html">Martin Fowler的角色接口</a>
 */
public abstract class BoundedDomainModel<UnboundedModel extends IUnboundedDomainModel> {
    protected final UnboundedModel model;

    /**
     * 限定上下文模型的构造器.
     * <p>
     * <p>Visibility by design：这意味着{@link IUnboundedDomainModel}与{@link BoundedDomainModel}必须在同一个package.</p>
     *
     * @param model 基于哪一个非限定上下文模型
     */
    protected BoundedDomainModel(UnboundedModel model) {
        this.model = model;
    }

    /**
     * The underlying unbounded domain model.
     */
    public final UnboundedModel unbounded() {
        return model;
    }
}
