package ddd.plus.showcase.wms.domain.common;

import io.github.dddplus.model.AbstractBusinessNo;
import lombok.NonNull;

/**
 * 库房.
 */
public class WarehouseNo extends AbstractBusinessNo<String> {
    private WarehouseNo(@NonNull String value) {
        super(value);
    }

    public static WarehouseNo of(@NonNull String warehouseNo) {
        return new WarehouseNo(warehouseNo);
    }
}
