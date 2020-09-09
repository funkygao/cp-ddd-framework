package org.ddd.cp.ddd.runtime.registry.mock.exception;

import org.ddd.cp.ddd.step.IDecideStepsException;
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
