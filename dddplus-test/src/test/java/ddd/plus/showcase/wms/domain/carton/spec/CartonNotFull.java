package ddd.plus.showcase.wms.domain.carton.spec;

import ddd.plus.showcase.wms.domain.carton.Carton;
import io.github.dddplus.model.spcification.AbstractSpecification;
import io.github.dddplus.model.spcification.Notification;

public class CartonNotFull extends AbstractSpecification<Carton> {
    @Override
    public boolean isSatisfiedBy(Carton carton, Notification notification) {
        return !carton.isFull();
    }
}
