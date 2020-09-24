package org.example.cp.oms.spec;

/**
 * 订单中台统一定义所有的活动和步骤.
 */
public interface Steps {

    interface SubmitOrder {
        String Activity = "submitOrder";

        String BasicStep = "basic";
        String ProductStep = "product";
        String PersistStep = "persist";
        String BroadcastStep = "mq";
    }

    interface CancelOrder {
        String Activity = "cancelOrder";

        String BasicStep = "basic";
        String StateStep = "state";
        String PersistStep = "persist";
    }
}
