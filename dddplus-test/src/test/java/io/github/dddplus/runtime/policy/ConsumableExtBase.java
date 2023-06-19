package io.github.dddplus.runtime.policy;

public abstract class ConsumableExtBase implements IConsumableExt {
    protected final String actualSku(String sku) {
        return "SKU-" + sku;
    }
}
