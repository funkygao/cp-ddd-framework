package ddd.plus.showcase.wms.infra.domain.common;

import ddd.plus.showcase.wms.domain.common.gateway.IInventoryGateway;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Setter(onMethod_ = {@Resource})
@Slf4j
public class InventoryGateway implements IInventoryGateway {
}
