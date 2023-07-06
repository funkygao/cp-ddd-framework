package ddd.plus.showcase.wms.domain.task;

import io.github.dddplus.model.BoundedDomainModel;
import lombok.experimental.Delegate;

public class ContainerItemBagPending extends BoundedDomainModel<ContainerItemBag> {

    ContainerItemBagPending(ContainerItemBag bag) {
        this.model = bag;
    }

    @Delegate
    ContainerItemBag model() {
        return unbounded();
    }

}
