package ddd.plus.showcase.wms.infrastructure.domain.common;

import ddd.plus.showcase.wms.domain.carton.Consumable;
import ddd.plus.showcase.wms.domain.common.gateway.IInventoryGateway;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Setter(onMethod_ = {@Resource})
@Slf4j
public class InventoryGateway implements IInventoryGateway {
    @Override
    public void deductConsumableInventory(List<Consumable> consumables) {

    }
}
