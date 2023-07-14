package ddd.plus.showcase.wms.infrastructure.domain.carton.association;

import ddd.plus.showcase.wms.domain.carton.Carton;
import ddd.plus.showcase.wms.domain.carton.Pallet;
import ddd.plus.showcase.wms.domain.carton.PalletNo;
import ddd.plus.showcase.wms.infrastructure.dao.Dao;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CartonPalletDb implements Carton.CartonPallet {
    private final PalletNo palletNo;
    private final Dao dao;

    @Override
    public Pallet get() {
        return dao.query("where pallet_no=?", palletNo.value());
    }

}
