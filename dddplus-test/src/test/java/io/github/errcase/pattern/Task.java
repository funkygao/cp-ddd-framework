package io.github.errcase.pattern;

import io.github.dddplus.model.IDomainModel;
import lombok.Getter;

public class Task implements IDomainModel {
    @Getter
    String taskType;
}
