package org.example.cp.oms.infra.resource;

import lombok.extern.slf4j.Slf4j;
import org.example.cp.oms.spec.resource.IStockService;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StockService implements IStockService {
    @Override
    public boolean preOccupyStock(String sku) {
        // 真实场景，会通过RPC/RESTful接口调用“库存中心”的服务接口
        // 这里仅仅是mock
        log.info("预占库存：{}", sku);
        return true;
    }
}
