package ddd.plus.showcase.wms.domain.carton.ext;

import ddd.plus.showcase.wms.domain.order.OrderBag;
import ddd.plus.showcase.wms.domain.task.Task;
import io.github.dddplus.annotation.Policy;
import io.github.dddplus.ext.IPolicy;
import lombok.NonNull;

@Policy
public class ConsumableExtPolicy implements IPolicy<IConsumableExt, Task> {
    public static final String CostFirst = "成本优先";
    public static final String SpeedFirst = "速度优先";

    @Override
    public String extensionCode(@NonNull Task task) {
        OrderBag pendingOrderBag = task.orders().pendingOrders();
        if (pendingOrderBag.consumableCostFirst()) {
            return CostFirst;
        }

        return SpeedFirst;
    }
}
