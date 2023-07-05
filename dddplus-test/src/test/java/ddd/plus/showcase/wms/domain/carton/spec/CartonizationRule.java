package ddd.plus.showcase.wms.domain.carton.spec;

import ddd.plus.showcase.wms.domain.carton.Carton;
import io.github.dddplus.model.spcification.AbstractSpecification;
import io.github.dddplus.model.spcification.Notification;

/**
 * 装箱规则.
 */
public class CartonizationRule extends AbstractSpecification<Carton> {

    @Override
    public boolean isSatisfiedBy(Carton candidate, Notification notification) {
        // 不同属性的货品不能同箱混放
        // 重量、体积不能超限
        // ...
        return true;
    }
}
