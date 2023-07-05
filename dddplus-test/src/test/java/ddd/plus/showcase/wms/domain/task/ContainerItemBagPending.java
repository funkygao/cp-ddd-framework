package ddd.plus.showcase.wms.domain.task;

import io.github.dddplus.model.BoundedDomainModel;
import lombok.experimental.Delegate;

import java.util.List;

public class ContainerItemBagPending extends BoundedDomainModel<ContainerItemBag> {

    ContainerItemBagPending(ContainerItemBag bag) {
        this.model = bag;
    }

    @Delegate
    ContainerItemBag getModel() {
        return model;
    }

    public List<ContainerItem> items() {
        return model.getItems();
    }
}
