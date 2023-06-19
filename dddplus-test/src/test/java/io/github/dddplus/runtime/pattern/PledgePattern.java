package io.github.dddplus.runtime.pattern;

import io.github.dddplus.annotation.Pattern;
import io.github.dddplus.runtime.BasePattern;
import lombok.extern.slf4j.Slf4j;

@Pattern(code = "pl")
@Slf4j
public class PledgePattern extends BasePattern {
    private Boolean match(ExTask task) {
        log.info("PledgePattern.-match(task)");
        return task.getPresaleFlag() != null && task.presaleFlag;
    }

    public boolean match(ExOrder order) {
        log.info("PledgePattern.+match(order)");
        return order.getMoneyCollected() != null && order.getMoneyCollected() < 10;
    }

    protected boolean match(ExCarton carton) {
        log.info("PledgePattern.#match(carton)");
        return carton != null;
    }
}
