package org.cdf.ddd.step;

import java.util.List;

/**
 * 框架提供的默认{@link IReviseStepsException}实现.
 */
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
