package ddd.plus.showcase.wms.domain.common;

import io.github.dddplus.model.AbstractBusinessNo;
import lombok.NonNull;

/**
 * 承运商.
 */
public class Carrier extends AbstractBusinessNo<String> {
    private String carrierName;
    private Integer carrierType;

    protected Carrier(@NonNull String carrierNo) {
        super(carrierNo);
    }

}
