package ddd.plus.showcase.wms.domain.order.ext;

import ddd.plus.showcase.wms.domain.order.IOrder;
import io.github.dddplus.ext.IDomainExtension;

public interface IOrderAllowShipExt extends IDomainExtension {

    /**
     * 特定业务场景下判断该订单是否允许出库.
     *
     * <p>为了降低实现者直接操作{@link ddd.plus.showcase.wms.domain.order.Order}产生意外负面效应，把它限制到{@link IOrder}</p>
     */
    Boolean allow(IOrder order);
}
