package ddd.plus.showcase.wms.domain.carton;

import ddd.plus.showcase.wms.domain.base.ListBag;
import io.github.dddplus.dsl.KeyRelation;

@KeyRelation(whom = Consumable.class, type = KeyRelation.Type.HasMany)
public class ConsumableBag extends ListBag<Consumable> {
}
