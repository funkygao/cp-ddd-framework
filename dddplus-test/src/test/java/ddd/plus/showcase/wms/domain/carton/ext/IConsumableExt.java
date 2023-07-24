package ddd.plus.showcase.wms.domain.carton.ext;

import ddd.plus.showcase.wms.domain.carton.Carton;
import ddd.plus.showcase.wms.domain.carton.ConsumableBag;
import ddd.plus.showcase.wms.domain.carton.ICarton;
import ddd.plus.showcase.wms.domain.task.ITask;
import io.github.dddplus.dsl.KeyFlow;
import io.github.dddplus.ext.IDomainExtension;

public interface IConsumableExt extends IDomainExtension {

    /**
     * 为纸箱推荐耗材
     */
    @KeyFlow(actor = Carton.class)
    ConsumableBag recommendFor(ITask task, ICarton carton);

}
