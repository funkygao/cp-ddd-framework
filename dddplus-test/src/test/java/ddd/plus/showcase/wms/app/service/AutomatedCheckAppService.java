package ddd.plus.showcase.wms.app.service;

import ddd.plus.showcase.wms.app.UnitOfWork;
import ddd.plus.showcase.wms.domain.carton.ICartonRepository;
import ddd.plus.showcase.wms.domain.common.IMasterDataGateway;
import ddd.plus.showcase.wms.domain.common.IOrderGateway;
import ddd.plus.showcase.wms.domain.order.IOrderRepository;
import ddd.plus.showcase.wms.domain.task.ITaskRepository;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 自动复核用例.
 */
@Service
@Setter(onMethod_ = {@Resource})
@Slf4j
public class AutomatedCheckAppService {
    private IMasterDataGateway masterDataGateway;
    private IOrderGateway orderGateway;

    private ITaskRepository taskRepository;
    private IOrderRepository orderRepository;
    private ICartonRepository cartonRepository;

    private UnitOfWork uow;

}
