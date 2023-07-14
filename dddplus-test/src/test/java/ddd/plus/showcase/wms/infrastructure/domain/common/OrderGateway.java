package ddd.plus.showcase.wms.infrastructure.domain.common;

import ddd.plus.showcase.wms.domain.common.gateway.IOrderGateway;
import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

// 这里虽然没有任何实现，但提供了参考的包结构设计
@Service
@Setter(onMethod_ = {@Resource})
@Slf4j
public class OrderGateway implements IOrderGateway {
    @Override
    public Set<OrderNo> canceledSubSet(Set<OrderNo> orderNos, WarehouseNo warehouseNo) {
        return null;
    }
}
