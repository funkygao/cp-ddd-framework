package io.github.errcase.pattern;

import io.github.dddplus.model.IDomainModel;
import io.github.dddplus.ext.IIdentity;
import lombok.Getter;
import lombok.Setter;

public class FooTask implements IDomainModel, IIdentity {
    @Getter
    @Setter
    String taskType;
}
