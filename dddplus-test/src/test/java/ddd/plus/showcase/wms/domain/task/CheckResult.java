package ddd.plus.showcase.wms.domain.task;

import io.github.dddplus.dsl.KeyRule;
import io.github.dddplus.model.IDomainModel;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * 复核结果.
 */
@AllArgsConstructor
public class CheckResult implements IDomainModel {
    private final ContainerItemBag bag;

    /**
     * 本次复核完的货品清单
     */
    @KeyRule
    public List<ContainerItem> items() {
        return bag.items();
    }

}
