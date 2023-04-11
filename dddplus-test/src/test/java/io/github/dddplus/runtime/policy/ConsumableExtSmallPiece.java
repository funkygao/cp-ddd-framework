package io.github.dddplus.runtime.policy;

import io.github.dddplus.annotation.Extension;

import java.util.HashMap;
import java.util.Map;

@Extension(code = ConsumableExtPolicy.SMALL_PIECE)
public class ConsumableExtSmallPiece extends ConsumableExtBase {
    private static Map<String, String> table = new HashMap<>();
    static {
        table.put("SKU-1", "CON-10");
        table.put("SKU-10", "CON-11");
    }
    @Override
    public String recommend(String sku) {
        String actualSku = super.actualSku(sku);
        return table.get(actualSku);
    }
}
