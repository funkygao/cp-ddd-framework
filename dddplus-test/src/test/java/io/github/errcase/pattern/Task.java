package io.github.errcase.pattern;

import io.github.dddplus.model.IDomainModel;
import io.github.dddplus.model.IIdentity;
import lombok.Getter;

public class Task implements IDomainModel, IIdentity {
    @Getter
    String taskType;
}
