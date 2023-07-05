package ddd.plus.showcase.wms.domain.common;

import io.github.dddplus.model.AbstractBusinessNo;
import lombok.NonNull;

/**
 * 货品的批次号.
 */
public class LotNo extends AbstractBusinessNo<String> {
    protected LotNo(@NonNull String value) {
        super(value);
    }

    public static LotNo of(@NonNull String lotNo) {
        return new LotNo(lotNo);
    }
}
