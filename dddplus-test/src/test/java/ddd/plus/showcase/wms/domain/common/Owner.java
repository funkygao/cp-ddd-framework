package ddd.plus.showcase.wms.domain.common;

import io.github.dddplus.model.AbstractBusinessNo;
import lombok.NonNull;

/**
 * 货主.
 */
public class Owner extends AbstractBusinessNo<String> {
    private String ownerName;

    protected Owner(@NonNull String value) {
        super(value);
    }

    public static Owner of(@NonNull String ownerNo) {
        return new Owner(ownerNo);
    }
}
