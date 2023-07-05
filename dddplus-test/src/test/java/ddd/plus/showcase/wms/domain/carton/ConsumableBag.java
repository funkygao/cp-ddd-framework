package ddd.plus.showcase.wms.domain.carton;

import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.model.ListBag;

import java.util.List;

@KeyRelation(whom = Consumable.class, type = KeyRelation.Type.HasMany)
public class ConsumableBag extends ListBag<Consumable> {
    protected ConsumableBag(List<Consumable> items) {
        super(items);
    }
}
