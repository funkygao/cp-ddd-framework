package ddd.plus.showcase.wms.domain.pack;

import ddd.plus.showcase.wms.domain.carton.Carton;
import ddd.plus.showcase.wms.domain.carton.CartonNo;
import ddd.plus.showcase.wms.domain.common.WmsException;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import ddd.plus.showcase.wms.domain.pack.dict.PackStatus;
import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.model.BaseAggregateRoot;
import io.github.dddplus.model.IUnboundedDomainModel;
import io.github.dddplus.model.spcification.Notification;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * 包裹.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Getter(AccessLevel.PACKAGE)
public class Pack extends BaseAggregateRoot<Pack> implements IUnboundedDomainModel {
    @KeyElement(types = KeyElement.Type.Structural, byType = true)
    private WaybillNo waybillNo;
    @KeyElement(types = KeyElement.Type.Structural, byType = true)
    private OrderNo orderNo;
    @KeyElement(types = KeyElement.Type.Lifecycle, byType = true)
    private PackStatus status;
    @KeyElement(types = KeyElement.Type.Billing)
    private BigDecimal totalWeight;
    @KeyElement(types = KeyElement.Type.Billing)
    private BigDecimal totalVolume;
    @KeyElement(types = KeyElement.Type.Contextual, remark = "包裹明细采集场景")
    @KeyRelation(whom = Carton.class, contextual = true, type = KeyRelation.Type.From, remark = "包裹明细采集")
    private CartonNo cartonNo;

    @Override
    protected void whenNotSatisfied(Notification notification) {
        throw new WmsException(notification.first());
    }

    @KeyBehavior
    public void fromCarton(Carton carton) {
        this.cartonNo = carton.getCartonNo();
    }
}
