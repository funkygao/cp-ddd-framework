package ddd.plus.showcase.wms.domain.ship.hint;

import ddd.plus.showcase.wms.domain.ship.ShipManifest;
import io.github.dddplus.model.IMergeAwareDirtyHint;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ShippedHint implements IMergeAwareDirtyHint<Long> {
    private ShipManifest shipManifest;

    @Override
    public Long getId() {
        return shipManifest.getId();
    }
}
