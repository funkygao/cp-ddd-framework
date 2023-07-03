package ddd.plus.showcase.wms.domain.order;

import ddd.plus.showcase.wms.domain.base.SetBag;
import ddd.plus.showcase.wms.domain.common.OrderGateway;
import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.model.IUnboundedDomainModel;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class OrderBag extends SetBag<Order> implements IUnboundedDomainModel {

    private OrderBag(Set<Order> orders) {
        this.items = orders;
    }

    public static OrderBag of(Set<Order> orders) {
        return new OrderBag(orders);
    }

    /**
     * 当前出库单集合里哪些已经在单据中心里被客户取消了.
     */
    @KeyBehavior
    public OrderBagCanceled subBagOfCanceled(OrderGateway gateway) {
        Set<OrderNo> canceledSet = gateway.canceledSet(orderNos(), warehouseNo());
        return new OrderBagCanceled(subBagOf(canceledSet));
    }

    private OrderBag subBagOf(Set<OrderNo> orderNoSet) {
        return OrderBag.of(items.stream().filter(o -> orderNoSet.contains(o.getOrderNo())).collect(Collectors.toSet()));
    }

    private Set<OrderNo> orderNos() {
        Set<OrderNo> set = new HashSet<>(size());
        for (Order order : items) {
            set.add(order.getOrderNo());
        }
        return set;
    }

    public WarehouseNo warehouseNo() {
        // 这里的订单一定都在同一个仓库
        return anyItem().getWarehouseNo();
    }
}
