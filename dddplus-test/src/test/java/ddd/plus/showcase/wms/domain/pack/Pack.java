package ddd.plus.showcase.wms.domain.pack;

import ddd.plus.showcase.wms.domain.common.BaseAggregateRoot;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import ddd.plus.showcase.wms.domain.pack.dict.PackStatus;
import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.model.IUnboundedDomainModel;

import java.math.BigDecimal;

/**
 * 包裹.
 */
public class Pack extends BaseAggregateRoot<Pack> implements IUnboundedDomainModel {
    @KeyElement(types = KeyElement.Type.Structural)
    private WaybillNo waybillNo;
    @KeyElement(types = KeyElement.Type.Structural)
    private OrderNo orderNo;
    @KeyElement(types = KeyElement.Type.Lifecycle, byType = true)
    private PackStatus status;
    @KeyElement(types = KeyElement.Type.Billing)
    private BigDecimal totalWeight;
    @KeyElement(types = KeyElement.Type.Billing)
    private BigDecimal totalVolume;
}
