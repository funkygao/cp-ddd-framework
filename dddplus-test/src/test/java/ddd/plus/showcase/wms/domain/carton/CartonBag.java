package ddd.plus.showcase.wms.domain.carton;

import ddd.plus.showcase.wms.domain.common.Operator;
import ddd.plus.showcase.wms.domain.common.Platform;
import io.github.dddplus.dsl.KeyBehavior;
import io.github.dddplus.model.ListBag;
import lombok.NonNull;

import java.util.List;

public class CartonBag extends ListBag<Carton> {
    protected CartonBag(List<Carton> items) {
        super(items);
    }

    public static CartonBag of(@NonNull List<Carton> cartons) {
        return new CartonBag(cartons);
    }

    @KeyBehavior
    public void fulfill(Operator operator, Platform platform) {
        items.forEach(c -> c.fulfill(operator, platform));
    }

    @KeyBehavior
    public void putOnPallet(PalletNo palletNo) {
        items.forEach(c -> c.putOnPallet(palletNo));
    }

    @KeyBehavior
    public void deductConsumableInventory() {
        items.forEach(c -> c.deductConsumableInventory());
    }

}
