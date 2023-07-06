package ddd.plus.showcase.wms.domain.common;

import io.github.dddplus.model.AbstractBusinessNo;
import lombok.NonNull;

/**
 * 收货人.
 */
public class Consignee extends AbstractBusinessNo<String> {
    protected Consignee(@NonNull String name) {
        super(name);
    }

}
