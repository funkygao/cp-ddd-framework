package io.github.design;

import io.github.dddplus.runtime.DDD;

public class MyService {

    public void doSth(ShipmentOrder order, CheckTask task) {
        DDD.usePolicy(SplitOrderExtPolicy.class, order).split(order, task);
    }
}
