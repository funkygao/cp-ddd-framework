package org.cdf.ddd.runtime.registry.mock.exception;

import lombok.Setter;
import org.cdf.ddd.step.IDecideStepsException;

import java.util.List;

public class FooDecideStepsException extends FooException implements IDecideStepsException {

    public FooDecideStepsException() {
        super();
    }

    public FooDecideStepsException(String message) {
        super(message);
    }

    @Setter
    private List<String> steps;

    public FooDecideStepsException withSubsequentSteps(List<String> subsequentSteps) {
        this.steps = subsequentSteps;
        return this;
    }

    @Override
    public List<String> subsequentSteps() {
        return steps;
    }
}
