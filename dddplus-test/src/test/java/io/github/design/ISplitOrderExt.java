package io.github.design;

import io.github.dddplus.ext.IDomainExtension;

public interface ISplitOrderExt extends IDomainExtension {
    void split(ShipmentOrder shipmentOrder, CheckTask checkTask);
}
