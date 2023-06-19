package com.liteflow.components.packflow.context;

import com.liteflow.components.packflow.domain.CartonBag;
import com.liteflow.components.packflow.domain.PlatformNo;
import com.liteflow.components.packflow.domain.ShipmentOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PackFlowContext {
    private final ShipmentOrder so;
    private final CartonBag cartonBag;
    private final PlatformNo platformNo;
}
