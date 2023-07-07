package ddd.plus.showcase.wms.domain.carton;

import io.github.dddplus.model.AbstractBusinessNo;
import lombok.NonNull;

/**
 * 栈板.
 */
public class PalletNo extends AbstractBusinessNo<String> {
    protected PalletNo(String value) {
        super(value);
    }

    public static PalletNo of(@NonNull String palletNo) {
        return new PalletNo(palletNo);
    }
}
