package io.github.badcase.ability.bad2;

import io.github.dddplus.annotation.DomainAbility;
import io.github.dddplus.ext.IDomainExtension;
import io.github.dddplus.model.IDomainModel;
import io.github.dddplus.runtime.BaseDomainAbility;
import io.github.dddplus.runtime.registry.mock.domain.FooDomain;
import lombok.extern.slf4j.Slf4j;

@DomainAbility(domain = FooDomain.CODE)
@Slf4j
public class IllegalGenericAbility extends BaseDomainAbility {

    @Override
    public IDomainExtension defaultExtension(IDomainModel model) {
        return null;
    }
}
