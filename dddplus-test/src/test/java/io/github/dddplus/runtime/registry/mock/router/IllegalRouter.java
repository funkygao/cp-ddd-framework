package io.github.dddplus.runtime.registry.mock.router;

import io.github.dddplus.runtime.BaseRouter;
import io.github.dddplus.runtime.registry.mock.ext.IFooExt;
import io.github.dddplus.runtime.registry.mock.model.FooModel;

import javax.validation.constraints.NotNull;

// @Router(domain = FooDomain.CODE, name = "foo")
public class IllegalRouter extends BaseRouter<FooModel, IFooExt> {

    public String submit(FooModel model) {
        String s1 = "submit received: " + String.valueOf(this.getExtension(model, null).execute(model));
        return s1 + ", firstExt got: " + String.valueOf(firstExtension(model).execute(model));
    }


    @Override
    public IFooExt defaultExtension(@NotNull FooModel model) {
        return null;
    }
}
