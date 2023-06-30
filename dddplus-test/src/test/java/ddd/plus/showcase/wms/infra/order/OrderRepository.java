package ddd.plus.showcase.wms.infra.order;

import ddd.plus.showcase.wms.domain.order.IOrderRepository;
import ddd.plus.showcase.wms.domain.order.Order;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository implements IOrderRepository {
    @Override
    public void save(Order order) {
    }
}
