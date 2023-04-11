package io.github.enforce.pattern_naming;

import io.github.dddplus.annotation.Pattern;
import io.github.enforce.FooIdentity;
import io.github.dddplus.runtime.BasePattern;

@Pattern(code = "foo")
public class Foo extends BasePattern {

    private boolean match(FooIdentity identity) {
        return true;
    }
}
