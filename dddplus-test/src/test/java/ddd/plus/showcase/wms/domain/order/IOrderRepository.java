package ddd.plus.showcase.wms.domain.order;

import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import io.github.dddplus.model.IRepository;

public interface IOrderRepository extends IRepository {

    Order mustGet(OrderNo orderNo, WarehouseNo warehouseNo);

    void save(Order order);

    void switchToCanceledStatus(OrderBagCanceled orderBagCanceled);

}
