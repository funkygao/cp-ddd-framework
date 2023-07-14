package io.github.badcase.router.bad2;

import io.github.dddplus.annotation.Router;
import io.github.dddplus.ext.IDomainExtension;
import io.github.dddplus.ext.IIdentity;
import io.github.dddplus.runtime.BaseRouter;
import io.github.dddplus.runtime.registry.mock.domain.FooDomain;
import lombok.extern.slf4j.Slf4j;

// 虽然没有明确指定具体的泛型，但它仍旧合法，Model is IDomainModel, Ext is IDomainExtension
@Router(domain = FooDomain.CODE)
@Slf4j
public class StillLegalGenericRouter extends BaseRouter {

    @Override
    public IDomainExtension defaultExtension(IIdentity model) {
        return null;
    }
}
