package ddd.plus.showcase.wms.domain.carton.spec;

import ddd.plus.showcase.wms.domain.carton.Carton;
import io.github.dddplus.buddy.specification.AbstractSpecification;
import io.github.dddplus.buddy.specification.Notification;

public class CaronNotFull extends AbstractSpecification<Carton> {
    @Override
    public boolean isSatisfiedBy(Carton carton, Notification notification) {
        return !carton.isFull();
    }
}
