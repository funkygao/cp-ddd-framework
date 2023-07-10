package ddd.plus.showcase.wms.domain.common.gateway;

import ddd.plus.showcase.wms.domain.common.Operator;
import ddd.plus.showcase.wms.domain.common.Platform;
import ddd.plus.showcase.wms.domain.common.Sku;
import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import ddd.plus.showcase.wms.domain.order.dict.OrderType;
import ddd.plus.showcase.wms.domain.task.dict.TaskMode;
import io.github.dddplus.model.IGateway;

import java.util.List;
import java.util.Set;

/**
 * 主数据RPC防腐层.
 */
public interface IMasterDataGateway extends IGateway {

    /**
     * 指定操作员是否有进行复核作业的权限.
     */
    boolean allowPerformChecking(Operator operator);

    /**
     * 满足指定(出库单类型，复核作业模式)条件的复核台有哪些.
     */
    List<Platform> candidatePlatforms(OrderType orderType, TaskMode taskMode, WarehouseNo warehouseNo);

    List<Sku> pullSkuBySkuNos(Set<String> skuNoSet);

}
