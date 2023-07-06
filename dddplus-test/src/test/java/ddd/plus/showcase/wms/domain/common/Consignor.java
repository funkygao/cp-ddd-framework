package ddd.plus.showcase.wms.domain.common;

import io.github.dddplus.model.AbstractBusinessNo;
import lombok.NonNull;

/**
 * 发货人.
 */
public class Consignor extends AbstractBusinessNo<String> {
    protected Consignor(@NonNull String name) {
        super(name);
    }

}
