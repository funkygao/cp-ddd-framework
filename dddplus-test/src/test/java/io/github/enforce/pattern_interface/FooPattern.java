package io.github.enforce.pattern_interface;

import io.github.dddplus.annotation.Pattern;
import io.github.enforce.FooIdentity;

@Pattern(code = "foo")
public class FooPattern {

    private boolean match(FooIdentity identity) {
        return true;
    }
}
