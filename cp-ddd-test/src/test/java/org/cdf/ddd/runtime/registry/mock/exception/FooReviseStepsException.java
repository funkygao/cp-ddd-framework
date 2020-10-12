package org.cdf.ddd.runtime.registry.mock.exception;

import lombok.Setter;
import org.cdf.ddd.step.IReviseStepsException;

import java.util.List;

public class FooReviseStepsException extends RuntimeException implements IReviseStepsException {

    public FooReviseStepsException() {
        super();
    }

    public FooReviseStepsException(String message) {
        super(message);
    }

    @Setter
    private List<String> steps;

    public FooReviseStepsException withSubsequentSteps(List<String> subsequentSteps) {
        this.steps = subsequentSteps;
        return this;
    }

    @Override
    public List<String> subsequentSteps() {
        return steps;
    }
}
