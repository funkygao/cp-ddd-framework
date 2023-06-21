package io.github.dddplus.runtime.policy;

import io.github.dddplus.annotation.Policy;
import lombok.NonNull;

@Policy
public class ConsumableExtPolicy extends ConsumableExtPolicyBase {
    public static final String SMALL_PIECE = "小件";
    public static final String LARGE_PIECE = "大件";

    @Override
    public String extensionCode(@NonNull SKU identity) {
        if (identity.getSku().startsWith("1")) {
            return SMALL_PIECE;
        }

        return LARGE_PIECE;
    }
}
