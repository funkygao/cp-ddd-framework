package ddd.plus.showcase.wms.infra.order;

import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import ddd.plus.showcase.wms.domain.order.IOrderRepository;
import ddd.plus.showcase.wms.domain.order.Order;
import ddd.plus.showcase.wms.domain.order.OrderBagCanceled;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository implements IOrderRepository {
    @Override
    public Order mustGet(OrderNo orderNo, WarehouseNo warehouseNo) {
        return null;
    }

    @Override
    public void save(Order order) {
    }

    @Override
    public void switchToCanceledStatus(OrderBagCanceled orderBagCanceled) {

    }
}
