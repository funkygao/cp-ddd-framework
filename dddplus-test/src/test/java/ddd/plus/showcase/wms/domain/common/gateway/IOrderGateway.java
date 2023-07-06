package ddd.plus.showcase.wms.domain.common.gateway;

import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import io.github.dddplus.model.IGateway;

import java.util.Set;

/**
 * 单据中心RPC的防腐层.
 */
public interface IOrderGateway extends IGateway {

    /**
     * 给定的出库单集合，客户已经被取消的子集是什么.
     *
     * <p>之所以有这样的设计是因为微服务架构下，有(单据中心，出库服务)两个系统，它们通过{@code RPC}才能状态同步.</p>
     */
    Set<OrderNo> canceledSubSet(Set<OrderNo> orderNos, WarehouseNo warehouseNo);
}
