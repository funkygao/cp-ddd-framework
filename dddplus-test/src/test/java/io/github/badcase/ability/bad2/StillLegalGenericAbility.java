package io.github.badcase.ability.bad2;

import io.github.dddplus.annotation.DomainAbility;
import io.github.dddplus.ext.IDomainExtension;
import io.github.dddplus.model.IDomainModel;
import io.github.dddplus.runtime.BaseDomainAbility;
import io.github.dddplus.runtime.registry.mock.domain.FooDomain;
import lombok.extern.slf4j.Slf4j;

// 虽然没有明确指定具体的泛型，但它仍旧合法，Model is IDomainModel, Ext is IDomainExtension
@DomainAbility(domain = FooDomain.CODE)
@Slf4j
public class StillLegalGenericAbility extends BaseDomainAbility {

    @Override
    public IDomainExtension defaultExtension(IDomainModel model) {
        return null;
    }
}
