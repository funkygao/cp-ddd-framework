package io.github.dddplus.runtime.registry.mock.router;

import io.github.dddplus.annotation.Router;
import io.github.dddplus.runtime.BaseRouter;
import io.github.dddplus.runtime.registry.mock.ext.IPullbackExt;
import io.github.dddplus.runtime.registry.mock.model.SaleOrder;
import lombok.NonNull;

@Router
public class PullbackExtRouter extends BaseRouter<IPullbackExt, SaleOrder> {

    public void blindlyExecuteAllExt(SaleOrder saleOrder) {
        forEachExtension(saleOrder).pullback();
    }

    @Override
    public IPullbackExt defaultExtension(@NonNull SaleOrder identity) {
        return null;
    }
}
