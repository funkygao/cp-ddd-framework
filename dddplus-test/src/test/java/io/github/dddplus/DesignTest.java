package io.github.dddplus;

import io.github.dddplus.runtime.DDD;
import io.github.design.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// 利用DDDplus进行设计
public class DesignTest {
    private PresalePattern presalePattern = new PresalePattern();
    private PledgePattern pledgePattern = new PledgePattern();

    @Test
    void essencePattern() {
        CheckTask.Essence essence = new CheckTask.Essence();
        CheckTask checkTask = essence.createCheckTask();
        // repo.save(checkTask);
    }

    @Test
    void matchAny() {
        CheckTask task = CheckTask.builder().build();
        assertFalse(task.matchAny(presalePattern));
        assertTrue(task.matchAny(pledgePattern));
        assertTrue(task.matchAny(presalePattern, pledgePattern));
    }

    void patternMutuallyExclusive(ShipmentOrder shipmentOrder, CheckTask checkTask) {
        DDD.useRouter(SplitOrderExtRouter.class).splitOrderMutuallyExclusive(shipmentOrder, checkTask);
    }

    void patternChainComposition(ShipmentOrder shipmentOrder, CheckTask checkTask) {
        DDD.useRouter(SplitOrderExtRouter.class).splitOrderChained(shipmentOrder, checkTask);
    }

    void patternAndPolicyUsedTogether(ShipmentOrder shipmentOrder, CheckTask checkTask) {
        // 目前看，Policy与Pattern只能2选1，无法做到透明切换，因为使用的时候就必须知道使用的哪一种
        DDD.usePolicy(SplitOrderExtPolicy.class, shipmentOrder).split(shipmentOrder, checkTask);
        DDD.useRouter(SplitOrderExtRouter.class).splitOrderMutuallyExclusive(shipmentOrder, checkTask);
    }

    void patternFilter() {
    }

}
