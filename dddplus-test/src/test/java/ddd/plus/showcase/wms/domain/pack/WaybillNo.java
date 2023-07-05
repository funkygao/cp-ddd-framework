package ddd.plus.showcase.wms.domain.pack;

import io.github.dddplus.model.AbstractBusinessNo;
import lombok.NonNull;

/**
 * 运单号.
 */
public class WaybillNo extends AbstractBusinessNo<String> {
    protected WaybillNo(@NonNull String value) {
        super(value);
    }

    public static WaybillNo of(@NonNull String waybillNo) {
        return new WaybillNo(waybillNo);
    }
}
