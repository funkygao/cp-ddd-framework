package io.github.dddplus.runtime.policy;

import io.github.dddplus.model.IIdentity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SKU implements IIdentity {
    private String sku;
}
