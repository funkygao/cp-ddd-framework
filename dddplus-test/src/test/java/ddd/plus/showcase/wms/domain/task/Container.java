package ddd.plus.showcase.wms.domain.task;

import ddd.plus.showcase.wms.domain.task.dict.ContainerType;
import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.dsl.KeyRelation;
import io.github.dddplus.model.IDomainModel;
import io.github.design.ContainerNo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 拣货容器.
 *
 * <p>运营生产环节，拣货人员把拣货容器(里面装有订单的sku)移动到复核台.</p>
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@KeyRelation(whom = ContainerItem.class, type = KeyRelation.Type.HasMany)
public class Container implements IDomainModel {
    private Long id;
    @KeyElement(types = KeyElement.Type.DCU, remark = "扫描枪可扫")
    private ContainerNo containerNo;
    private ContainerType type;

    private ContainerItemBag containerItemBag;

}
