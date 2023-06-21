package io.github.design;

import io.github.dddplus.annotation.Pattern;
import io.github.dddplus.runtime.BasePattern;

@Pattern(code = "presale")
public class PresalePattern extends BasePattern {

    boolean match(CheckTask checkTask) {
        return false;
    }

    boolean match(ShipmentOrder so) {
        return true;
    }
}
