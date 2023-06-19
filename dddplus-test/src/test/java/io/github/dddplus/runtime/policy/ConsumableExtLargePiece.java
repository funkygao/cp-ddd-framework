package io.github.dddplus.runtime.policy;

import io.github.dddplus.annotation.Extension;

import java.util.HashMap;
import java.util.Map;

@Extension(code = ConsumableExtPolicy.LARGE_PIECE)
public class ConsumableExtLargePiece extends ConsumableExtBase {
    private static Map<String, String> table = new HashMap<>();
    static {
        table.put("SKU-2", "CON-20");
        table.put("SKU-20", "CON-211");
    }
    @Override
    public String recommend(String sku) {
        if (sku.equals("2")) {
            throw new MyBusinessException("hi 2");
        }

        String actualSku = super.actualSku(sku);
        return table.get(actualSku);
    }
}
