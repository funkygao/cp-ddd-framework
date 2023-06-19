package io.github.design;

import io.github.dddplus.annotation.Policy;
import io.github.dddplus.ext.IPolicy;
import lombok.NonNull;

@Policy
public class SplitOrderExtPolicy implements IPolicy<ISplitOrderExt, ShipmentOrder> {
    @Override
    public String extensionCode(@NonNull ShipmentOrder identity) {
        return null;
    }
}
