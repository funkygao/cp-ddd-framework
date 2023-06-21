package io.github.design;

import io.github.dddplus.model.AbstractBusinessNo;
import lombok.NonNull;

public class ContainerNo extends AbstractBusinessNo<String> {
    public ContainerNo(@NonNull String value) {
        super(value);
    }
}
