package io.github.design;

import io.github.dddplus.dsl.KeyFlow;
import io.github.dddplus.model.IDomainService;
import org.springframework.stereotype.Component;

@Component
public class CheckTaskDomainService implements IDomainService {

    /**
     * foobar
     */
    @KeyFlow(actor = CheckTask.class, produceEvent = CheckTaskFinished.class)
    public void check() {

    }

    public String helo() {
        return "bye";
    }
}
