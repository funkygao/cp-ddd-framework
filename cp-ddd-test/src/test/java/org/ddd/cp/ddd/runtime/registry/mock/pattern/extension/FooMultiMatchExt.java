package org.ddd.cp.ddd.runtime.registry.mock.pattern.extension;

import org.ddd.cp.ddd.annotation.Extension;
import org.ddd.cp.ddd.runtime.registry.mock.ext.IMultiMatchExt;
import org.ddd.cp.ddd.runtime.registry.mock.pattern.FooPattern;

@Extension(code = FooPattern.CODE)
public class FooMultiMatchExt implements IMultiMatchExt {
}
