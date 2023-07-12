package ddd.plus.showcase.wms.infrastructure.domain.common;

import ddd.plus.showcase.wms.domain.common.Sku;
import ddd.plus.showcase.wms.domain.common.gateway.IMasterDataGateway;
import ddd.plus.showcase.wms.domain.common.Operator;
import ddd.plus.showcase.wms.domain.common.Platform;
import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import ddd.plus.showcase.wms.domain.order.dict.OrderType;
import ddd.plus.showcase.wms.domain.task.dict.TaskMode;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Service
@Setter(onMethod_ = {@Resource})
@Slf4j
public class MasterDataGateway implements IMasterDataGateway {
    @Override
    public boolean allowPerformChecking(Operator operator) {
        return false;
    }

    @Override
    public List<Platform> candidatePlatforms(OrderType orderType, TaskMode taskMode, WarehouseNo warehouseNo) {
        return null;
    }

    @Override
    public List<Sku> pullSkuBySkuNos(Set<String> skuNoSet) {
        return null;
    }
}
