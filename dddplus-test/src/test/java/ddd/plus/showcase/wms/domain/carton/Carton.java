package ddd.plus.showcase.wms.domain.carton;

import ddd.plus.showcase.wms.domain.common.BaseAggregateRoot;
import ddd.plus.showcase.wms.domain.task.Task;
import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.model.IUnboundedDomainModel;

@KeyRelation(whom = CartonItem.class, type = KeyRelation.Type.HasMany)
@KeyRelation(whom = Consumable.class, type = KeyRelation.Type.HasMany)
@KeyRelation(whom = Pallet.class, type = KeyRelation.Type.BelongTo, remark = "可能")
@KeyRelation(whom = Task.class, type = KeyRelation.Type.BelongTo)
public class Carton extends BaseAggregateRoot<Carton> implements IUnboundedDomainModel {
    private Long id;
    private CartonNo cartonNo;

    @KeyBehavior
    public void fullize() {

    }
}
