package ddd.plus.showcase.wms.domain.diff;

import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.model.ListBag;
import lombok.NonNull;

import java.util.List;

@KeyRelation(whom = ContainerDiffItem.class, type = KeyRelation.Type.HasMany)
public class ContainerDiffItemBag extends ListBag<ContainerDiffItem> {
    protected ContainerDiffItemBag(@NonNull List<ContainerDiffItem> items) {
        super(items);
    }
}
