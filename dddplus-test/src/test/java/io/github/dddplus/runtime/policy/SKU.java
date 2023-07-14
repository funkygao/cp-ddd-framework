package io.github.dddplus.runtime.policy;

import io.github.dddplus.ext.IIdentity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SKU implements IIdentity {
    private String sku;
}
