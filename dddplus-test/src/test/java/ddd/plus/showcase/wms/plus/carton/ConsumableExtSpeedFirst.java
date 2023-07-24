package ddd.plus.showcase.wms.plus.carton;

import ddd.plus.showcase.wms.domain.carton.ConsumableBag;
import ddd.plus.showcase.wms.domain.carton.ICarton;
import ddd.plus.showcase.wms.domain.carton.ext.IConsumableExt;
import ddd.plus.showcase.wms.domain.carton.ext.ConsumableExtPolicy;
import ddd.plus.showcase.wms.domain.task.ITask;
import io.github.dddplus.annotation.Extension;

@Extension(code = ConsumableExtPolicy.SpeedFirst)
public class ConsumableExtSpeedFirst implements IConsumableExt {
    @Override
    public ConsumableBag recommendFor(ITask task, ICarton carton) {
        return null;
    }
}
