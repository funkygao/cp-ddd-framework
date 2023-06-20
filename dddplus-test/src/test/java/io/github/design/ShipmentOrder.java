package io.github.design;

import io.github.dddplus.model.IDomainModel;
import io.github.dddplus.model.IIdentity;
import lombok.Getter;

public class ShipmentOrder implements IIdentity, IDomainModel {
    @Getter
    private Long id;
}
