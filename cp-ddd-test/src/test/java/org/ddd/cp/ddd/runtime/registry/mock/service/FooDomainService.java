package org.ddd.cp.ddd.runtime.registry.mock.service;

import lombok.extern.slf4j.Slf4j;
import org.ddd.cp.ddd.annotation.DomainService;
import org.ddd.cp.ddd.model.IDomainService;
import org.ddd.cp.ddd.runtime.DDD;
import org.ddd.cp.ddd.runtime.registry.mock.ability.FooDomainAbility;
import org.ddd.cp.ddd.runtime.registry.mock.domain.FooDomain;
import org.ddd.cp.ddd.runtime.registry.mock.model.FooModel;

@DomainService(domain = FooDomain.CODE)
@Slf4j
public class FooDomainService implements IDomainService {

    public void submitOrder(FooModel model) {
        log.debug("submitOrder");
        FooDomainAbility ability = DDD.findAbility(FooDomainAbility.class);
        log.info(ability.submit(model));
    }

}
