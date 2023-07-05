package ddd.plus.showcase.wms.domain.carton;

import ddd.plus.showcase.wms.domain.common.UniqueCode;
import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.model.ListBag;
import lombok.NonNull;

import java.util.List;

@KeyRelation(whom = CartonItem.class, type = KeyRelation.Type.HasMany)
public class CartonItemBag extends ListBag<CartonItem> {

    protected CartonItemBag(List<CartonItem> items) {
        super(items);
    }

    void appendAll(List<CartonItem> items) {
        this.items.addAll(items);
    }

    /**
     * 货品唯一码是否在本箱里已有了
     */
    @KeyBehavior(useRawArgs = true)
    public boolean contains(@NonNull UniqueCode uniqueCode) {
        for (CartonItem item : items) {
            if (uniqueCode.equals(item.getUniqueCode())) {
                return true;
            }
        }
        return false;
    }
}
