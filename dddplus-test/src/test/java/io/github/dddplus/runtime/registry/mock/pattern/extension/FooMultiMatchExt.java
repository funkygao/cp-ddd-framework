package io.github.dddplus.runtime.registry.mock.pattern.extension;

import io.github.dddplus.annotation.Extension;
import io.github.dddplus.runtime.registry.mock.ext.IMultiMatchExt;
import io.github.dddplus.runtime.registry.mock.pattern.FooPattern;

@Extension(code = FooPattern.CODE)
public class FooMultiMatchExt implements IMultiMatchExt {
}
