package ddd.plus.showcase.wms.domain.carton.ext.impl;

import ddd.plus.showcase.wms.domain.carton.Carton;
import ddd.plus.showcase.wms.domain.carton.ConsumableBag;
import ddd.plus.showcase.wms.domain.carton.ext.ConsumableExt;
import ddd.plus.showcase.wms.domain.carton.ext.ConsumableExtPolicy;
import ddd.plus.showcase.wms.domain.task.Task;
import io.github.dddplus.annotation.Extension;

@Extension(code = ConsumableExtPolicy.SpeedFirst)
public class ConsumableExtSpeedFirst implements ConsumableExt {
    @Override
    public ConsumableBag recommendFor(Task task, Carton carton) {
        return null;
    }
}
