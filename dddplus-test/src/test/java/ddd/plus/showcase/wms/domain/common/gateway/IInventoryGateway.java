package ddd.plus.showcase.wms.domain.common.gateway;

import ddd.plus.showcase.wms.domain.carton.Consumable;
import io.github.dddplus.model.IGateway;

import java.util.List;

/**
 * 库存RPC防腐层.
 */
public interface IInventoryGateway extends IGateway {
    void deductConsumableInventory(List<Consumable> consumables);
}
