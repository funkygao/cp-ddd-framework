package io.github.design;

import io.github.dddplus.annotation.Router;
import io.github.dddplus.runtime.BaseRouter;
import lombok.NonNull;

@Router
public class SplitOrderExtRouter extends BaseRouter<ISplitOrderExt, ShipmentOrder> {

    public void splitOrderMutuallyExclusive(ShipmentOrder shipmentOrder, CheckTask checkTask) {
        // 基于Pattern也可以实现垂直互斥，而非水平叠加
        firstExtension(shipmentOrder).split(shipmentOrder, checkTask);
    }

    // 一个Router可以支持多种路由模式：互斥 or 叠加
    public void splitOrderChained(ShipmentOrder shipmentOrder, CheckTask checkTask) {
    }

    @Override
    public ISplitOrderExt defaultExtension(@NonNull ShipmentOrder identity) {
        // return null意味着默认逻辑是啥也不做
        return null;
    }
}
