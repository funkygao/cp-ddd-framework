package io.github.errcase.invalidpattern;

import io.github.dddplus.annotation.Pattern;

// 该Pattern故意没有实现IIdentityResolver
@Pattern(code = InvalidPattern.CODE, name = "B2B模式", priority = 90)
public class InvalidPattern {
    public static final String CODE = "b2b";

}
