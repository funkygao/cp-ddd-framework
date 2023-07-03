package ddd.plus.showcase.wms.domain.carton;

import ddd.plus.showcase.wms.domain.base.ListBag;
import ddd.plus.showcase.wms.domain.common.UniqueCode;
import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.dsl.KeyRelation;
import lombok.NonNull;

@KeyRelation(whom = CartonItem.class, type = KeyRelation.Type.HasMany)
public class CartonItemBag extends ListBag<CartonItem> {

    /**
     * 货品唯一是否在本箱里已有了
     */
    @KeyBehavior
    public boolean contains(@NonNull UniqueCode uniqueCode) {
        for (CartonItem item : items) {
            if (uniqueCode.equals(item.getUniqueCode())) {
                return true;
            }
        }
        return false;
    }
}
