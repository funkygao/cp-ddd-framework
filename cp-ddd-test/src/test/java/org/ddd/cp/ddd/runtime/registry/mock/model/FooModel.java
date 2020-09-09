package org.ddd.cp.ddd.runtime.registry.mock.model;

import org.ddd.cp.ddd.model.IDomainModel;
import lombok.Data;

@Data
public class FooModel implements IDomainModel {
    private String projectCode;

    private boolean b2c;

    private boolean willSleepLong;

    private boolean willThrowRuntimeException;

    private boolean redecide;

    private boolean stepsRevised;

}
