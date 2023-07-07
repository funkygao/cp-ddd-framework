package ddd.plus.showcase.wms.domain.common.flow.handler;

import ddd.plus.showcase.wms.domain.carton.event.IFlowAutomationEvent;
import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import ddd.plus.showcase.wms.domain.order.IOrderRepository;
import ddd.plus.showcase.wms.domain.order.Order;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import ddd.plus.showcase.wms.domain.order.event.OrderCheckedEvent;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OrderCheckedEventHandler extends AbstractEventHandler {
    private IOrderRepository orderRepository;

    @Override
    protected void processMyEvent(IFlowAutomationEvent request) {
        OrderCheckedEvent event = (OrderCheckedEvent) request;
        Order order = orderRepository.mustGet(OrderNo.of(event.getOrderNo()), WarehouseNo.of(event.getWarehouseNo()));
        if (!order.constraint().isAutoPack()) {
            return;
        }

        // 执行打包的数据流，与实物流分离

        if (successor != null) {
            // trigger successor
            successor.processEvent(request);
        }
    }

    @Override
    protected boolean isMine(IFlowAutomationEvent event) {
        return event instanceof OrderCheckedEvent;
    }
}
