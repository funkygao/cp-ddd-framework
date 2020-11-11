package io.github.dddplus.runtime.registry.mock.extension;

import io.github.dddplus.annotation.Extension;
import io.github.dddplus.runtime.registry.mock.ext.IPartnerOnlyExt;
import io.github.dddplus.runtime.registry.mock.partner.FooPartner;

@Extension(code = FooPartner.CODE)
public class PartnerOnlyExt implements IPartnerOnlyExt {
    @Override
    public void foo() {

    }
}
