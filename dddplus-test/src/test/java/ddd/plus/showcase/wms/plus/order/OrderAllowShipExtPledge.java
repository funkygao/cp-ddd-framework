package ddd.plus.showcase.wms.plus.order;

import ddd.plus.showcase.wms.domain.common.pattern.PledgePattern;
import ddd.plus.showcase.wms.domain.order.IOrder;
import ddd.plus.showcase.wms.domain.order.ext.IOrderAllowShipExt;
import io.github.dddplus.annotation.Extension;

@Extension(code = PledgePattern.CODE)
public class OrderAllowShipExtPledge implements IOrderAllowShipExt {
    @Override
    public Boolean allow(IOrder order) {
        // write your logic here
        return true;
    }
}
