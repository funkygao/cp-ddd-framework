package org.ddd.cp.ddd.runtime.registry.mock.step;

public final class Steps {
    private Steps() {}

    public final class Submit {
        public static final String Activity = "Submit";
        public static final String GoodsValidationGroup = "goodsValidation";

        public static final String FooStep = "Foo";
        public static final String BarStep = "Bar";
        public static final String BazStep = "Baz";
        public static final String HamStep = "Ham";
    }

    public final class Cancel {
        public static final String Activity = "Cancel";

        public static final String EggStep = "Egg";
    }
}
