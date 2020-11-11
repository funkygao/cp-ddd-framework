package io.github.dddplus.runtime.registry.mock.extension;

import io.github.dddplus.annotation.Extension;
import io.github.dddplus.runtime.registry.mock.ext.IPatternOnlyExt;
import io.github.dddplus.runtime.registry.mock.pattern.B2BPattern;

@Extension(code = B2BPattern.CODE)
public class PatternOnlyExt implements IPatternOnlyExt {

}
