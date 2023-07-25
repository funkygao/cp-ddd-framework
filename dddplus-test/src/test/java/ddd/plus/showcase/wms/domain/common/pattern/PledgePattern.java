package ddd.plus.showcase.wms.domain.common.pattern;

import ddd.plus.showcase.wms.domain.common.Owner;
import io.github.dddplus.annotation.Pattern;
import io.github.dddplus.runtime.BasePattern;
import org.springframework.beans.factory.annotation.Value;

/**
 * 仓内质押模式.
 */
@Pattern(code = PledgePattern.CODE)
public class PledgePattern extends BasePattern {
    public static final String CODE = "ob.pledge";

    @Value("${owner.pledge:}")
    private String ownerNoWhitelist;

    boolean match(Owner owner) {
        for (String pledged : ownerNoWhitelist.split(":")) {
            if (owner.value().equals(pledged)) {
                return true;
            }
        }

        return false;
    }
}
