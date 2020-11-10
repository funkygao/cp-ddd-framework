package io.github.dddplus.runtime.registry.mock.model;

import io.github.dddplus.model.IDomainModel;
import lombok.Data;

@Data
public class FooModel implements IDomainModel {
    private String partnerCode;

    private boolean b2c;

    private boolean letFooThrowException;

    private boolean willSleepLong;

    private boolean willThrowRuntimeException;

    private boolean willThrowOOM;

    private boolean redecide;

    private boolean redecideDeadLoop;

    private boolean stepsRevised;

    private boolean willRollback;

    private boolean willRollbackInvalid;

}
