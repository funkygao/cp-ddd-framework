package org.cdf.ddd.runtime.registry.mock.pattern.extension;

import org.cdf.ddd.annotation.Extension;
import org.cdf.ddd.runtime.registry.mock.ext.IMultiMatchExt;
import org.cdf.ddd.runtime.registry.mock.pattern.FooPattern;

@Extension(code = FooPattern.CODE)
public class FooMultiMatchExt implements IMultiMatchExt {
}
