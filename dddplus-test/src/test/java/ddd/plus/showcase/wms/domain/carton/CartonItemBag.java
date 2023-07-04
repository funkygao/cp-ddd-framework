package ddd.plus.showcase.wms.domain.carton;

import io.github.dddplus.model.ListBag;
import ddd.plus.showcase.wms.domain.common.UniqueCode;
import ddd.plus.showcase.wms.domain.common.WmsException;
import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.model.spcification.Notification;
import lombok.NonNull;

import java.util.List;

@KeyRelation(whom = CartonItem.class, type = KeyRelation.Type.HasMany)
public class CartonItemBag extends ListBag<CartonItem> {

    protected CartonItemBag(List<CartonItem> items) {
        super(items);
    }

    @Override
    protected void whenNotSatisfied(Notification notification) {
        throw new WmsException(notification.first());
    }

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
