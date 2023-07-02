package io.github.design;

import io.github.dddplus.dsl.KeyFlow;
import io.github.dddplus.ext.IDomainExtension;

public interface ISplitOrderExt extends IDomainExtension {

    @KeyFlow(actor = ShipmentOrder.class, polymorphism = true)
    void split(ShipmentOrder shipmentOrder, CheckTask checkTask);
}
