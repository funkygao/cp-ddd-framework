package ddd.plus.showcase.wms.domain.common;

import ddd.plus.showcase.wms.domain.order.OrderNo;
import io.github.dddplus.model.IGateway;

import java.util.Set;

/**
 * 单据中心RPC的防腐层.
 */
public interface OrderGateway extends IGateway {
    Set<OrderNo> canceledSet(Set<OrderNo> orderNos, WarehouseNo warehouseNo);
}
