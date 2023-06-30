package io.github.design;

import io.github.dddplus.dsl.KeyFlow;
import io.github.dddplus.runtime.DDD;

public class MyService {

    @KeyFlow(actor = God.class, async = true)
    public void doSth(ShipmentOrder order, CheckTask task) {
        DDD.usePolicy(SplitOrderExtPolicy.class, order).split(order, task);
    }
}
