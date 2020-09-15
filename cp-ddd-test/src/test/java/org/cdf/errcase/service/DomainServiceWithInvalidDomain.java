package org.cdf.errcase.service;

import lombok.extern.slf4j.Slf4j;
import org.cdf.ddd.annotation.DomainService;
import org.cdf.ddd.model.IDomainService;
import org.cdf.ddd.runtime.DDD;
import org.cdf.ddd.runtime.registry.mock.ability.FooDomainAbility;
import org.cdf.ddd.runtime.registry.mock.model.FooModel;

@DomainService(domain = "non-exist")
@Slf4j
public class DomainServiceWithInvalidDomain implements IDomainService {

    public void submitOrder(FooModel model) {
        FooDomainAbility ability = DDD.findAbility(FooDomainAbility.class);
        log.info(ability.submit(model));
    }

}
