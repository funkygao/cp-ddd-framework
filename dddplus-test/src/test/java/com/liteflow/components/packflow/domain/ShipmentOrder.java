package com.liteflow.components.packflow.domain;

import io.github.dddplus.model.IUnboundedDomainModel;

public class ShipmentOrder implements IUnboundedDomainModel {

    public boolean useWaybillNoFromOrder() {
        return false;
    }

    public boolean needFirstWaybillFromOrder(Integer packageQty) {
        return true;
    }
}
