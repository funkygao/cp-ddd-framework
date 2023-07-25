package ddd.plus.showcase.wms.plus.order;

import ddd.plus.showcase.wms.domain.common.pattern.PresalePattern;
import ddd.plus.showcase.wms.domain.order.IOrder;
import ddd.plus.showcase.wms.domain.order.ext.IOrderAllowShipExt;
import io.github.dddplus.annotation.Extension;

@Extension(code = PresalePattern.CODE)
public class OrderAllowShipExtPresale implements IOrderAllowShipExt {
    @Override
    public Boolean allow(IOrder order) {
        // write your logic here
        return true;
    }
}
