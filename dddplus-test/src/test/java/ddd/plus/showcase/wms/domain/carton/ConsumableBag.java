package ddd.plus.showcase.wms.domain.carton;

import ddd.plus.showcase.wms.domain.common.WmsException;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.model.ListBag;
import io.github.dddplus.model.spcification.Notification;

import java.util.List;

@KeyRelation(whom = Consumable.class, type = KeyRelation.Type.HasMany)
public class ConsumableBag extends ListBag<Consumable> {
    protected ConsumableBag(List<Consumable> items) {
        super(items);
    }

    @Override
    protected void whenNotSatisfied(Notification notification) {
        throw new WmsException(notification.first());
    }
}
