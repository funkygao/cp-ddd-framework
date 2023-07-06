package ddd.plus.showcase.wms.domain.pack;

import io.github.dddplus.dsl.KeyRule;
import io.github.dddplus.model.ListBag;
import lombok.NonNull;

import java.util.List;

public class PackBag extends ListBag<Pack> {
    protected PackBag(@NonNull List<Pack> items) {
        super(items);
    }

    public static PackBag of(@NonNull List<Pack> packs) {
        return new PackBag(packs);
    }

    @KeyRule
    public boolean isOnePack() {
        return size() == 1;
    }
}
