package io.github.design;

import io.github.dddplus.model.AbstractBusinessNo;
import lombok.NonNull;

public class StationNo extends AbstractBusinessNo<String> {
    public StationNo(@NonNull String value) {
        super(value);
    }
}
