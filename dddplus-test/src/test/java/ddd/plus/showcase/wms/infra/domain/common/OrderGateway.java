package ddd.plus.showcase.wms.infra.domain.common;

import ddd.plus.showcase.wms.domain.common.IOrderGateway;
import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

@Service
@Setter(onMethod_ = {@Resource})
@Slf4j
public class OrderGateway implements IOrderGateway {
    @Override
    public Set<OrderNo> canceledSet(Set<OrderNo> orderNos, WarehouseNo warehouseNo) {
        return null;
    }
}
