package ddd.plus.showcase.wms.domain.common.pattern;

import ddd.plus.showcase.wms.domain.order.Order;
import io.github.dddplus.annotation.Pattern;
import io.github.dddplus.runtime.BasePattern;

/**
 * 预售模式.
 */
@Pattern(code = PresalePattern.CODE)
public class PresalePattern extends BasePattern {
    public static final String CODE = "ob.presale";

    boolean match(Order order) {
        return order.getFeature().charAt(5) == '5';
    }
}
