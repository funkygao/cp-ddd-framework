/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.step;

import java.util.List;

/**
 * 框架提供的默认{@link IReviseStepsException}实现.
 */
@Deprecated
public class ReviseStepsException extends RuntimeException implements IReviseStepsException {

    private List<String> subsequentSteps;

    public ReviseStepsException withSubsequentSteps(List<String> subsequentSteps) {
        this.subsequentSteps = subsequentSteps;
        return this;
    }

    @Override
    public List<String> subsequentSteps() {
        return subsequentSteps;
    }
}
