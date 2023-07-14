package ddd.plus.showcase.wms.plus.carton;

import ddd.plus.showcase.wms.domain.carton.ConsumableBag;
import ddd.plus.showcase.wms.domain.carton.ICarton;
import ddd.plus.showcase.wms.domain.carton.ext.ConsumableExt;
import ddd.plus.showcase.wms.domain.carton.ext.ConsumableExtPolicy;
import ddd.plus.showcase.wms.domain.task.ITask;
import io.github.dddplus.annotation.Extension;

@Extension(code = ConsumableExtPolicy.CostFirst)
public class ConsumableExtCostFirst implements ConsumableExt {
    @Override
    public ConsumableBag recommendFor(ITask task, ICarton carton) {
        return null;
    }
}
