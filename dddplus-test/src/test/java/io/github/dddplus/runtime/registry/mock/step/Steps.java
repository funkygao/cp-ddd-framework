package io.github.dddplus.runtime.registry.mock.step;

public interface Steps {

    interface Submit {
        String Activity = "Submit";
        String GoodsValidationGroup = "goodsValidation";

        String FooStep = "Foo";
        String BarStep = "Bar";
        String BazStep = "Baz";
        String HamStep = "Ham";
    }

    interface Cancel {
        String Activity = "Cancel";

        String EggStep = "Egg";
    }
}
