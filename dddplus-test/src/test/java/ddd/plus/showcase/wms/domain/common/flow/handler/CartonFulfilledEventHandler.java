package ddd.plus.showcase.wms.domain.common.flow.handler;

import ddd.plus.showcase.wms.domain.carton.Carton;
import ddd.plus.showcase.wms.domain.carton.CartonNo;
import ddd.plus.showcase.wms.domain.carton.ICartonRepository;
import ddd.plus.showcase.wms.domain.carton.event.CartonFulfilledEvent;
import ddd.plus.showcase.wms.domain.carton.event.IFlowAutomationEvent;
import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CartonFulfilledEventHandler extends AbstractEventHandler {
    private ICartonRepository cartonRepository;

    @Override
    public void processMyEvent(IFlowAutomationEvent request) {
        CartonFulfilledEvent event = (CartonFulfilledEvent) request;
        Carton carton = cartonRepository.mustGet(CartonNo.of(event.getCartonNo()),
                WarehouseNo.of(event.getWarehouseNo()));
        if (!carton.order().get().constraint().isAutoShip()) {
            // 这意味着后续自动流程也不必执行
            return;
        }

        // 执行自动发货
        // ...

        if (successor != null) {
            // trigger successor
            successor.processEvent(request);
        }
    }

    @Override
    protected boolean isMine(IFlowAutomationEvent event) {
        return event instanceof CartonFulfilledEvent;
    }
}
