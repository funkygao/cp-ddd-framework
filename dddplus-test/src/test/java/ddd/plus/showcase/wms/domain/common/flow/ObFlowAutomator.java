package ddd.plus.showcase.wms.domain.common.flow;

import ddd.plus.showcase.wms.domain.carton.ICartonRepository;
import ddd.plus.showcase.wms.domain.common.flow.handler.TaskAcceptedEventHandler;
import ddd.plus.showcase.wms.domain.common.publisher.IFlowAutomationEvent;
import ddd.plus.showcase.wms.domain.common.flow.handler.AbstractEventHandler;
import ddd.plus.showcase.wms.domain.common.flow.handler.CartonFulfilledEventHandler;
import ddd.plus.showcase.wms.domain.common.flow.handler.OrderCheckedEventHandler;
import ddd.plus.showcase.wms.domain.order.IOrderRepository;
import ddd.plus.showcase.wms.domain.task.ITaskRepository;
import ddd.plus.showcase.wms.domain.task.event.TaskAcceptedEvent;
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
    private ITaskRepository taskRepository;
    private AbstractEventHandler head;

    @KeyFlow
    public void orchestrate(IFlowAutomationEvent event) {
        head.processEvent(event);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        TaskAcceptedEventHandler taskAcceptedEventHandler = new TaskAcceptedEventHandler(taskRepository);
        CartonFulfilledEventHandler cartonFulfilledEventHandler = new CartonFulfilledEventHandler(cartonRepository);
        OrderCheckedEventHandler orderCheckedEventHandler = new OrderCheckedEventHandler(orderRepository);
        taskAcceptedEventHandler.setSuccessor(cartonFulfilledEventHandler);
        cartonFulfilledEventHandler.setSuccessor(orderCheckedEventHandler);

        head = taskAcceptedEventHandler;
    }
}
