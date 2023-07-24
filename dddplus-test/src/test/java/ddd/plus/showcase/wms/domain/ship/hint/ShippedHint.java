package ddd.plus.showcase.wms.domain.ship.hint;

import ddd.plus.showcase.wms.domain.ship.ShipManifest;
import io.github.dddplus.model.IDirtyHint;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ShippedHint implements IDirtyHint {
    private ShipManifest shipManifest;
}
