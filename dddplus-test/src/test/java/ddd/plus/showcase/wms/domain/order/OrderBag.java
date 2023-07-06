package ddd.plus.showcase.wms.domain.order;

import ddd.plus.showcase.wms.domain.common.gateway.IOrderGateway;
import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import ddd.plus.showcase.wms.domain.common.WmsException;
import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.model.IUnboundedDomainModel;
import io.github.dddplus.model.SetBag;
import io.github.dddplus.model.spcification.ISpecification;
import io.github.dddplus.model.spcification.Notification;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class OrderBag extends SetBag<Order> implements IUnboundedDomainModel {

    protected OrderBag(Set<Order> orders) {
        super(orders);
    }

    static OrderBag of(Set<Order> orders) {
        return new OrderBag(orders);
    }

    public void satisfy(ISpecification<Order> spec) {
        Notification notification = Notification.build();
        for (Order order : items) {
            if (!spec.isSatisfiedBy(order, notification)) {
                throw new WmsException(notification.first());
            }
        }
    }

    /**
     * 当前出库单集合里哪些已经在单据中心里被客户取消了.
     */
    @KeyBehavior
    public OrderBagCanceled canceledBag(IOrderGateway gateway) {
        Set<OrderNo> canceledSet = gateway.canceledSubSet(orderNos(), warehouseNo());
        return new OrderBagCanceled(bagOf(canceledSet));
    }

    private OrderBag bagOf(Set<OrderNo> orderNoSet) {
        return OrderBag.of(items.stream().filter(o -> orderNoSet.contains(o.getOrderNo())).collect(Collectors.toSet()));
    }

    private Set<OrderNo> orderNos() {
        Set<OrderNo> set = new HashSet<>(size());
        for (Order order : items) {
            set.add(order.getOrderNo());
        }
        return set;
    }

    private WarehouseNo warehouseNo() {
        // 这里的订单一定都在同一个仓库
        return anyItem().getWarehouseNo();
    }
}
