package ddd.plus.showcase.wms.domain.common.flow.handler;

import ddd.plus.showcase.wms.domain.common.publisher.IFlowAutomationEvent;
import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import ddd.plus.showcase.wms.domain.order.IOrderRepository;
import ddd.plus.showcase.wms.domain.order.Order;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import ddd.plus.showcase.wms.domain.order.event.OrderCheckedEvent;
import io.github.dddplus.dsl.KeyFlow;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OrderCheckedEventHandler extends AbstractEventHandler<OrderCheckedEvent> {
    private IOrderRepository orderRepository;

    @Override
    @KeyFlow(actor = Order.class)
    protected void processMyEvent(OrderCheckedEvent event) {
        Order order = orderRepository.mustGet(OrderNo.of(event.getOrderNo()), WarehouseNo.of(event.getWarehouseNo()));
        if (!order.constraint().isAutoPack()) {
            return;
        }

        // 执行打包的数据流，与实物流分离

        triggerSuccessor(event);
    }

    @Override
    protected boolean isMine(IFlowAutomationEvent event) {
        return event instanceof OrderCheckedEvent;
    }

}
