package ddd.plus.showcase.wms.application.service;

import ddd.plus.showcase.wms.application.UnitOfWork;
import ddd.plus.showcase.wms.application.convert.ShippingAppConverter;
import ddd.plus.showcase.wms.application.service.dto.ShipOrderRequest;
import ddd.plus.showcase.wms.application.service.dto.base.ApiResponse;
import ddd.plus.showcase.wms.domain.carton.CartonBag;
import ddd.plus.showcase.wms.domain.carton.ICartonRepository;
import ddd.plus.showcase.wms.domain.common.ISequencer;
import ddd.plus.showcase.wms.domain.common.Operator;
import ddd.plus.showcase.wms.domain.common.WarehouseNo;
import ddd.plus.showcase.wms.domain.order.IOrderRepository;
import ddd.plus.showcase.wms.domain.order.Order;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import ddd.plus.showcase.wms.domain.order.spec.OrderShippingReady;
import ddd.plus.showcase.wms.domain.ship.OrderCarton;
import ddd.plus.showcase.wms.domain.ship.ShipManifest;
import io.github.dddplus.model.IApplicationService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 业务用例：发货
 */
@Service
@Setter(onMethod_ = {@Resource})
@Slf4j
public class ShippingAppService implements IApplicationService {
    private IOrderRepository orderRepository;
    private ICartonRepository cartonRepository;
    private ISequencer sequencer;
    private UnitOfWork uow;

    /**
     * 整单发货.
     */
    @KeyUsecase
    public ApiResponse<Void> shipOrder(@Valid ShipOrderRequest dto) {
        OrderNo orderNo = OrderNo.of(dto.getOrderNo());
        WarehouseNo warehouseNo = WarehouseNo.of(dto.getWarehouseNo());

        Order order = orderRepository.mustGet(orderNo, warehouseNo);
        order.assureSatisfied(new OrderShippingReady());

        // 订单完整性校验：订单的货品可能分散在不同拣货区，经过合流、不同复核台，此时它们凑齐了吗？
        // 是否可以发货校验：预售订单，客户是否缴齐尾款？

        ShipManifest shipManifest = ShippingAppConverter.INSTANCE.toShippingManifest(dto);

        // 该订单的所有货都已经装在这些纸箱了，并且已经填充好耗材了的
        CartonBag cartonBag = cartonRepository.mustGet(orderNo, warehouseNo);
        List<OrderCarton> orderCartons = OrderCarton.createFrom(cartonBag);
        shipManifest.ship(orderCartons);

        // 扣减库存

        order.ship(Operator.of(dto.getOperatorNo()));

        uow.persist(shipManifest, order);
        return ApiResponse.ofOk();
    }

}
