package ddd.plus.showcase.wms.domain.order.ext;

import ddd.plus.showcase.wms.domain.order.Order;
import io.github.dddplus.annotation.Router;
import io.github.dddplus.runtime.BaseRouter;
import io.github.dddplus.runtime.IReducer;
import lombok.NonNull;

import java.util.function.Predicate;

@Router
public class OrderAllowShipExtRouter extends BaseRouter<IOrderAllowShipExt, Order> {

    public boolean allowShip(Order order) {
        Predicate<Boolean> stopper = allow -> allow != null && !allow;
        Boolean allow = forEachExtension(order, IReducer.stopOnFirstMatch(stopper))
                .allow(order);
        if (allow == null) {
            // 可以没有扩展点要对发货条件限制
            return true;
        }

        return allow;
    }

    @Override
    public IOrderAllowShipExt defaultExtension(@NonNull Order identity) {
        return null;
    }
}
