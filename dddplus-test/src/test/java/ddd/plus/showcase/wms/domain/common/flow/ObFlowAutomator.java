package ddd.plus.showcase.wms.domain.common.flow;

import ddd.plus.showcase.wms.domain.carton.ICartonRepository;
import ddd.plus.showcase.wms.domain.carton.event.IFlowAutomationEvent;
import ddd.plus.showcase.wms.domain.common.flow.handler.AbstractEventHandler;
import ddd.plus.showcase.wms.domain.common.flow.handler.CartonFulfilledEventHandler;
import ddd.plus.showcase.wms.domain.common.flow.handler.OrderCheckedEventHandler;
import ddd.plus.showcase.wms.domain.common.flow.handler.SomeEventHandler;
import ddd.plus.showcase.wms.domain.order.IOrderRepository;
import io.github.dddplus.dsl.KeyFlow;
import io.github.dddplus.model.INativeFlow;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 出库(Outbound)的流程自动化串联.
 */
@Component
@Setter(onMethod_ = {@Resource})
@Slf4j
public class ObFlowAutomator implements INativeFlow, InitializingBean {
    private ICartonRepository cartonRepository;
    private IOrderRepository orderRepository;
    private AbstractEventHandler head;

    @KeyFlow
    public void orchestrate(IFlowAutomationEvent event) {
        head.processEvent(event);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        AbstractEventHandler cartonFulfilledEventHandler = new CartonFulfilledEventHandler(cartonRepository);
        AbstractEventHandler someEventHandler = new SomeEventHandler();
        AbstractEventHandler orderCheckedEventHandler = new OrderCheckedEventHandler(orderRepository);
        cartonFulfilledEventHandler.setSuccessor(orderCheckedEventHandler);
        orderCheckedEventHandler.setSuccessor(someEventHandler);

        head = cartonFulfilledEventHandler;
    }
}
