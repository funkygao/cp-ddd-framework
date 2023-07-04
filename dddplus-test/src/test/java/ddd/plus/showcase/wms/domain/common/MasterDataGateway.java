package ddd.plus.showcase.wms.domain.common;

import ddd.plus.showcase.wms.domain.order.dict.OrderType;
import ddd.plus.showcase.wms.domain.task.dict.TaskMode;
import io.github.dddplus.model.IGateway;

import java.util.List;

/**
 * 主数据RPC防腐层.
 */
public interface MasterDataGateway extends IGateway {

    boolean allowPerformChecking(Operator operator);

    List<Platform> candidatePlatforms(OrderType orderType, TaskMode taskMode, WarehouseNo warehouseNo);

}
