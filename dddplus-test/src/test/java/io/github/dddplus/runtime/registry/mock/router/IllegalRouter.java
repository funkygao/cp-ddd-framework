package io.github.dddplus.runtime.registry.mock.router;

import io.github.dddplus.runtime.BaseRouter;
import io.github.dddplus.runtime.registry.mock.ext.IFooExt;
import io.github.dddplus.runtime.registry.mock.model.FooModel;
import lombok.NonNull;

// @Router(domain = FooDomain.CODE, name = "foo")
public class IllegalRouter extends BaseRouter<IFooExt, FooModel> {

    public String submit(FooModel model) {
        String s1 = "submit received: " + String.valueOf(this.forEachExtension(model).execute(model));
        return s1 + ", firstExt got: " + String.valueOf(firstExtension(model).execute(model));
    }


    @Override
    public IFooExt defaultExtension(@NonNull FooModel model) {
        return null;
    }
}
