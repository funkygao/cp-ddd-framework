package io.github.dddplus.runtime.registry.mock.router;

import io.github.dddplus.annotation.Router;
import io.github.dddplus.runtime.BaseRouter;
import io.github.dddplus.runtime.registry.mock.domain.FooDomain;
import io.github.dddplus.runtime.registry.mock.ext.IPartnerExt;
import io.github.dddplus.runtime.registry.mock.model.FooModel;

import javax.validation.constraints.NotNull;

@Router(domain = FooDomain.CODE, name = "partner")
public class PartnerRouter extends BaseRouter<FooModel, IPartnerExt> {

    public String submit(FooModel model) {
        return String.valueOf(firstExtension(model).execute(model));
    }

    @Override
    public IPartnerExt defaultExtension(@NotNull FooModel model) {
        return null;
    }
}
