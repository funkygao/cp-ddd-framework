package org.cdf.ddd.runtime.registry.mock.model;

import org.cdf.ddd.model.IDomainModel;
import lombok.Data;

@Data
public class FooModel implements IDomainModel {
    private String projectCode;

    private boolean b2c;

    private boolean willSleepLong;

    private boolean willThrowRuntimeException;

    private boolean willThrowOOM;

    private boolean redecide;

    private boolean redecideDeadLoop;

    private boolean stepsRevised;

    private boolean willRollback;

    private boolean willRollbackInvalid;

}
