package io.github.design;

import io.github.dddplus.dsl.KeyFlow;
import io.github.dddplus.model.IDomainService;

public class CheckTaskDomainService implements IDomainService {

    @KeyFlow(actor = CheckTask.class)
    public void check() {

    }
}
