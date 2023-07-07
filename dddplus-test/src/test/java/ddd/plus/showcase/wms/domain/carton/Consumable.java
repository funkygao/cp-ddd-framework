package ddd.plus.showcase.wms.domain.carton;

import ddd.plus.showcase.wms.domain.common.Operator;
import ddd.plus.showcase.wms.domain.common.Platform;
import ddd.plus.showcase.wms.domain.common.Sku;
import ddd.plus.showcase.wms.domain.order.OrderNo;
import ddd.plus.showcase.wms.domain.task.TaskNo;
import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.dsl.KeyRelation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

@KeyRelation(whom = Sku.class, type = KeyRelation.Type.Extends)
@Getter(AccessLevel.PACKAGE)
public class Consumable extends Sku {
    @KeyElement(types = KeyElement.Type.Structural, byType = true)
    private TaskNo taskNo;
    @KeyElement(types = KeyElement.Type.Structural, byType = true)
    private OrderNo orderNo;
    @KeyElement(types = KeyElement.Type.Structural, byType = true)
    private CartonNo cartonNo;
    @KeyElement(types = KeyElement.Type.Location, byType = true)
    private Platform platform;
    private Operator operator;
    // 该耗材是否有库存管理
    private boolean inventory;

    protected Consumable(@NonNull String skuNo) {
        super(skuNo);
    }

    public static Consumable of(@NonNull String skuNo) {
        return new Consumable(skuNo);
    }

    void bind(Carton carton) {
        this.taskNo = carton.getTaskNo();
        this.orderNo = carton.getOrderNo();
        this.cartonNo = carton.getCartonNo();
        this.platform = carton.getPlatform();
        this.operator = carton.getOperator();
    }
}
