package ddd.plus.showcase.wms.domain.carton;

import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.model.ListBag;

import java.util.List;
import java.util.stream.Collectors;

@KeyRelation(whom = Consumable.class, type = KeyRelation.Type.HasMany)
public class ConsumableBag extends ListBag<Consumable> {
    protected ConsumableBag(List<Consumable> items) {
        super(items);
    }

    /**
     * 受库存管理的耗材子集.
     */
    ConsumableBag inventoryControlBag() {
        return new ConsumableBag(items.stream()
                .filter(Consumable::isInventory)
                .collect(Collectors.toList()));
    }
}
