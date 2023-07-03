package ddd.plus.showcase.wms.domain.carton;

import ddd.plus.showcase.wms.domain.common.Sku;
import io.github.dddplus.dsl.KeyRelation;
import lombok.NonNull;

@KeyRelation(whom = Sku.class, type = KeyRelation.Type.Extends)
public class Consumable extends Sku {

    protected Consumable(@NonNull String skuNo) {
        super(skuNo);
    }

    public static Consumable of(@NonNull String skuNo) {
        return new Consumable(skuNo);
    }
}
