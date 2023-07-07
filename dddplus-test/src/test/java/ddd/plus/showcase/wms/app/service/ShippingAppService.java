package ddd.plus.showcase.wms.app.service;

import io.github.dddplus.model.IApplicationService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 业务用例：发货
 */
@Service
@Setter(onMethod_ = {@Resource})
@Slf4j
public class ShippingAppService implements IApplicationService {

}
