package io.github.design;

import io.github.dddplus.annotation.Pattern;
import io.github.dddplus.runtime.BasePattern;

@Pattern(code = "pledge")
public class PledgePattern extends BasePattern {
    boolean match(CheckTask checkTask) {
        return true;
    }

    boolean match(ShipmentOrder so) {
        return false;
    }
}
