package ddd.plus.showcase.wms.domain.task;

import io.github.dddplus.model.BoundedDomainModel;
import lombok.experimental.Delegate;

import java.util.List;

public class ContainerItemBagPending extends BoundedDomainModel<ContainerItemBag> {
    protected ContainerItemBagPending(ContainerItemBag model) {
        super(model);
    }

    @Delegate
    ContainerItemBag getModel() {
        return model;
    }

    public List<ContainerItem> items() {
        return model.getItems();
    }
}
