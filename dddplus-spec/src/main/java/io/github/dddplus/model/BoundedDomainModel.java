/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model;

import io.github.dddplus.ext.IDomainExtension;

/**
 * 限定上下文的领域模型，又称角色对象(Methodful Role)/特定场景对象：角色对象承担相应的角色责任.
 *
 * <p>具体场景相关，运营相关，承载对应场景下的(数据，规则，行为)，不能脱离{@link IUnboundedDomainModel}独立存在，高内聚：它的代码改动只来源于该场景上下文需求</p>
 * <p>如果对象的某些行为在任何场景都是通用的，那么就放在{@link IUnboundedDomainModel}，将其绑定，这是尊重"共性"的约束；如果某些行为依赖于具体场景，就赋予相应角色形成{@link BoundedDomainModel}，这是尊重"个性"的自由.</p>
 * <p>{@link IUnboundedDomainModel}通过扮演某个角色进入某个特定场景时拥有的属性或独立行为，成为{@link BoundedDomainModel}</p>
 * <p>{@link IUnboundedDomainModel}回答了"它是什么"这个问题，不易变；{@link BoundedDomainModel}回答了"它做了什么"这个问题，易变，聚焦于场景特有行为</p>
 * <ul>使用规范：
 * <li>一个{@link IUnboundedDomainModel}的多个{@link BoundedDomainModel}间要满足正交性，不能有重叠：不能角色错位</li>
 * <li>一次交互可以涉及多角色对象吗？可以，例如下面示例代码里，Buyer把购买到的商品赠送给朋友Contact</li>
 * <li>{@code if xxx order.asXxx() else if yyy order.asYyy()}，这样使用角色对象允许吗？不允许。角色对象解决确定性场景问题，不确定性问题交给扩展点</li>
 * <li>两个角色对象有相同的行为，只是内部实现不同，可以吗？不可以，使用{@link IDomainExtension}解决多态问题。区别不同角色的是Role Method(扮演各自角色时，只能做出符合自己角色身份的行为)，而不是同一个Method的不同实现(怎么做)</li>
 * <li>一个类之所以是角色对象，是因为它具有符合该角色身份的行为，例如：Teacher可以上课，而User不会</li>
 * <li>是什么 vs 做什么 vs 做不做 vs 怎么做</li>
 * </ul>
 * <pre>
 * {@code
 *
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
 * 进阶应用：不仅普通的{@link IDomainModel}可以设计出角色对象，{@link IBag}也可以。
 * <pre>
 * {@code
 *
 * class ShipmentOrderBag implements IUnboundedDomainModel {
 *     // gateway是远程RPC的防腐层
 *     public ShipmentOrderBagContextRemote inContextOfRemote(ShipmentOrderGateway gateway) {
 *         return new ShipmentOrderBagContextRemote(this, gateway);
 *     }
 * }
 * // 这个订单集合数据存在远程的【订单中心】，通过RPC交互
 * class ShipmentOrderBagContextRemote extends BoundedDomainModel<ShipmentOrderBag> {
 *     private final ShipmentOrderGateway gateway;
 *     ShipmentOrderBagContextRemote(ShipmentOrderBag model, ShipmentOrderGateway gateway) {
 *         super(model);
 *         this.gateway = gateway;
 *     }
 *
 *     // 获取订单许可：其实是锁单，返回失败的单号
 *     public ShipmentOrderBag acquireProductionLicence() {
 *         Set<String> abnormalSoNoSet = gateway.acquireProductionLicence(OrderProductionNode.GENERATE_PACKAGE, model.orderNos(), model.warehouseNo());
 *         ShipmentOrderBag abnormalShipmentOrderBag = model.filterOf(abnormalSoNoSet);
 *         return abnormalShipmentOrderBag;
 *     }
 *     public void releaseProductionLicense() {}
 * }
 * class AppService {
 *     void doSth() {
 *         ShipmentOrderBagContextRemote remote = shipmentOrderBag.inContextOfRemote(shipmentOrderGateway);
 *         // 远程模型被显性化了，而不是隐藏在过程式方法里；而且，它内部可以有状态
 *         ShipmentOrderBag abnormalBag = remote.acquireProductionLicence();
 *         shipmentOrderRepository.modifyStatusToException(abnormalBag);
 *     }
 * }
 * }
 * </pre>
 * @see <a href="https://martinfowler.com/bliki/RoleInterface.html">Martin Fowler的角色接口</a>
 */
public abstract class BoundedDomainModel<UnboundedModel extends IUnboundedDomainModel> {
    protected UnboundedModel model;

    /**
     * The underlying unbounded domain model.
     */
    public UnboundedModel unbounded() {
        return model;
    }
}
