package io.github.errcase.pattern;

import io.github.dddplus.model.IPatternAwareModel;
import lombok.Getter;

public class Task implements IPatternAwareModel {
    @Getter
    String taskType;
}
