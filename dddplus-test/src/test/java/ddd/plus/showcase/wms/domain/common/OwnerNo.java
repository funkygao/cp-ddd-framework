package ddd.plus.showcase.wms.domain.common;

import io.github.dddplus.model.AbstractBusinessNo;
import lombok.NonNull;

/**
 * 货主编号.
 */
public class OwnerNo extends AbstractBusinessNo<String> {
    protected OwnerNo(@NonNull String value) {
        super(value);
    }

    public static OwnerNo of(@NonNull String ownerNo) {
        return new OwnerNo(ownerNo);
    }
}
