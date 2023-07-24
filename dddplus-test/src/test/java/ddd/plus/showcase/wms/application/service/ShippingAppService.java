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
import ddd.plus.showcase.wms.domain.common.WmsException;
import ddd.plus.showcase.wms.domain.order.IOrderRepository;
import ddd.plus.showcase.wms.domain.order.Order;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import ddd.plus.showcase.wms.domain.order.ext.OrderAllowShipExtRouter;
import ddd.plus.showcase.wms.domain.order.spec.OrderIntegrity;
import ddd.plus.showcase.wms.domain.order.spec.OrderShippingReady;
import ddd.plus.showcase.wms.domain.ship.ShipManifest;
import io.github.dddplus.dsl.KeyUsecase;
import io.github.dddplus.model.IApplicationService;
import io.github.dddplus.runtime.DDD;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.Valid;

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
    public ApiResponse<Void> shipOrder(@Valid ShipOrderRequest dto) throws WmsException {
        OrderNo orderNo = OrderNo.of(dto.getOrderNo());
        WarehouseNo warehouseNo = WarehouseNo.of(dto.getWarehouseNo());

        Order order = orderRepository.mustGet(orderNo, warehouseNo);
        order.assureSatisfied(new OrderShippingReady()
                .and(new OrderIntegrity()));

        /**
         * 长尾场景的额外校验
         *
         * 预售订单，客户是否缴齐尾款？仓内质押，货主credit是否有效？...
         * 链式校验，直到有一个say no
         */
        if (!DDD.useRouter(OrderAllowShipExtRouter.class).allowShip(order)) {
            throw new WmsException(WmsException.Code.ShipNotAllowed);
        }

        ShipManifest shipManifest = ShippingAppConverter.INSTANCE.toShippingManifest(dto); // dangling
        // 该订单的所有货都已经装在这些纸箱了，并且已经填充好耗材了的
        CartonBag cartonBag = cartonRepository.mustGet(orderNo, warehouseNo);
        shipManifest.loadForOrder(order, cartonBag);
        shipManifest.ship();

        // 扣减库存

        order.ship(Operator.of(dto.getOperatorNo()));

        uow.persist(shipManifest, order);
        return ApiResponse.ofOk();
    }

}
