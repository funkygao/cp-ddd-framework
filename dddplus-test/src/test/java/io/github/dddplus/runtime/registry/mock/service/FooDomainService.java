package io.github.dddplus.runtime.registry.mock.service;

import io.github.dddplus.annotation.DomainService;
import io.github.dddplus.model.IDomainService;
import lombok.extern.slf4j.Slf4j;
import io.github.dddplus.runtime.DDD;
import io.github.dddplus.runtime.registry.mock.ability.FooDomainAbility;
import io.github.dddplus.runtime.registry.mock.domain.FooDomain;
import io.github.dddplus.runtime.registry.mock.model.FooModel;

@DomainService(domain = FooDomain.CODE)
@Slf4j
public class FooDomainService implements IDomainService {

    public void submitOrder(FooModel model) {
        FooDomainAbility ability = DDD.findAbility(FooDomainAbility.class);
        log.info(ability.submit(model));
    }

}
