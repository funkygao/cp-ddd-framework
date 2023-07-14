package ddd.plus.showcase.wms.infrastructure.domain.carton.association;

import ddd.plus.showcase.wms.domain.carton.Carton;
import ddd.plus.showcase.wms.domain.order.Order;
import ddd.plus.showcase.wms.infrastructure.dao.Dao;
import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.dsl.KeyElement;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CartonOrderDb implements Carton.CartonOrder {
    @NonNull
    @KeyElement(types = KeyElement.Type.Structural, byType = true)
    private final Carton carton;
    @NonNull
    private final Dao dao;

    private Order cache;

    /**
     * 如何实现BelongTo关联关系，并且通过缓存支持多次调用.
     */
    @Override
    @KeyBehavior
    public Order get() {
        if (cache == null) {
            cache = dao.query("xxx");
        }
        return cache;
    }
}
