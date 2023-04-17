package io.github.errcase.invalidpattern;

import io.github.dddplus.annotation.Pattern;

@Pattern(code = "foo", resolver = false)
public class FooAppService {
    public void helo() {

    }
}
