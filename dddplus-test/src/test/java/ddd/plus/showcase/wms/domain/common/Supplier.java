package ddd.plus.showcase.wms.domain.common;

import io.github.dddplus.model.AbstractBusinessNo;
import lombok.NonNull;

/**
 * 供应商.
 */
public class Supplier extends AbstractBusinessNo<String> {
    private String supplierName;

    protected Supplier(@NonNull String supplierNo) {
        super(supplierNo);
    }

}
