package ddd.plus.showcase.wms.domain.task;

import ddd.plus.showcase.wms.domain.common.Sku;
import ddd.plus.showcase.wms.domain.task.dict.ContainerType;
import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.model.IDomainModel;
import io.github.design.ContainerNo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 拣货容器.
 *
 * <p>一个拣货容器里货品可能属于不同的{@link ddd.plus.showcase.wms.domain.order.Order}.</p>
 * <p>运营生产环节，拣货人员把拣货容器(里面装有订单的sku)移动到复核台.</p>
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter(AccessLevel.PACKAGE)
public class Container implements IDomainModel {
    private Long id;

    @KeyElement(types = KeyElement.Type.DCU, remark = "扫描枪可扫")
    private ContainerNo containerNo;
    @KeyElement(types = KeyElement.Type.Operational, byType = true)
    private ContainerType type;

    @KeyRelation(whom = ContainerItemBag.class, type = KeyRelation.Type.HasOne)
    private ContainerItemBag containerItemBag;

    Set<String> skuNoSet() {
        return containerItemBag.items().stream()
                .map(ContainerItem::getSku)
                .map(Sku::value)
                .collect(Collectors.toSet());
    }
}
