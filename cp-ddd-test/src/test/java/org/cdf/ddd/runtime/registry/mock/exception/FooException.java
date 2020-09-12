package org.cdf.ddd.runtime.registry.mock.exception;

import org.cdf.ddd.step.IDecideStepsException;
import lombok.Setter;

import java.util.List;

public class FooException extends RuntimeException implements IDecideStepsException {

    @Setter
    private List<String> steps;

    @Override
    public List<String> subsequentSteps() {
        return steps;
    }
}
