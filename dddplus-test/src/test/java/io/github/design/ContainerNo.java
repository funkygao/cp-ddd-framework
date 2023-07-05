package io.github.design;

import io.github.dddplus.model.AbstractBusinessNo;
import lombok.NonNull;

public class ContainerNo extends AbstractBusinessNo<String> {
    private ContainerNo(@NonNull String value) {
        super(value);
    }

    public static ContainerNo of(@NonNull String containerNo) {
        return new ContainerNo((containerNo));
    }
}
