package io.github.errcase.pattern;

import io.github.dddplus.model.IPatternAwareModel;
import lombok.Getter;

public class Order implements IPatternAwareModel {
    @Getter
    String upstream;
}
