package ddd.plus.showcase.wms.domain.carton;

import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.model.ListBag;

import java.util.List;

@KeyRelation(whom = CartonItem.class, type = KeyRelation.Type.HasMany)
public class CartonItemBag extends ListBag<CartonItem> {

    protected CartonItemBag(List<CartonItem> items) {
        super(items);
    }

    void appendAll(List<CartonItem> items) {
        this.items.addAll(items);
    }

}
