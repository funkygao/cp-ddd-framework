package ddd.plus.showcase.wms.domain.ship;

import ddd.plus.showcase.wms.domain.carton.Carton;
import ddd.plus.showcase.wms.domain.carton.CartonBag;
import ddd.plus.showcase.wms.domain.common.Carrier;
import ddd.plus.showcase.wms.domain.common.WmsException;
import ddd.plus.showcase.wms.domain.order.Order;
import ddd.plus.showcase.wms.domain.ship.dict.ShipStatus;
import ddd.plus.showcase.wms.domain.ship.hint.ShippedHint;
import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.model.BaseAggregateRoot;
import io.github.dddplus.model.IUnboundedDomainModel;
import io.github.dddplus.model.spcification.Notification;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 一辆货车的装车清单.
 *
 * <p>里面有很多订单：{@link OrderCarton}.</p>
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Getter(AccessLevel.PACKAGE)
public class ShipManifest extends BaseAggregateRoot<ShipManifest> implements IUnboundedDomainModel {
    @Getter
    private Long id;

    @KeyElement(types = KeyElement.Type.Lifecycle)
    private ShipStatus shipStatus;
    @KeyElement(types = KeyElement.Type.Structural, byType = true)
    private Carrier carrier;
    private String truckNo; // 车牌号
    private String driverName; // 司机姓名
    private String driverPhone; // 司机电话
    @KeyRelation(whom = OrderCarton.class, type = KeyRelation.Type.HasMany)
    private List<OrderCarton> orderCartons;
    @KeyElement(types = KeyElement.Type.Reserved)
    private Map<String, Object> extInfo;

    @Override
    protected void whenNotSatisfied(Notification notification) {
        throw new WmsException(notification.first());
    }

    // 发货
    @KeyBehavior
    public void ship() {
        shipStatus = ShipStatus.Shipped;
        mergeDirtyWith(new ShippedHint(this));
    }

    // 为订单装车
    @KeyBehavior(useRawArgs = true)
    public void loadForOrder(Order order, CartonBag cartonBag) {
        List<OrderCarton> orderCartons = new ArrayList<>(cartonBag.size());
        for (Carton carton : cartonBag.items()) {
            OrderCarton orderCarton = new OrderCarton();
            orderCarton.setCartonNo(carton.getCartonNo());
            orderCarton.setOrderNo(order.getOrderNo());
            // ...
        }
        this.orderCartons = orderCartons;
        mergeDirtyWith(new ShippedHint(this));
    }
}
