package ddd.plus.showcase.wms.domain.common;

import io.github.dddplus.model.AbstractBusinessNo;
import lombok.NonNull;

/**
 * 货品的唯一码.
 */
public class UniqueCode extends AbstractBusinessNo<String> {
    protected UniqueCode(@NonNull String value) {
        super(value);
    }

    public static UniqueCode of(@NonNull String uniqueCode) {
        return new UniqueCode(uniqueCode);
    }
}
