package org.ddd.cp.ddd.runtime.registry.mock.exception;

import org.ddd.cp.ddd.step.IReviseStepsException;
import lombok.Setter;

import java.util.List;

public class FooException extends RuntimeException implements IReviseStepsException {

    @Setter
    private List<String> steps;

    @Override
    public List<String> subsequentSteps() {
        return steps;
    }
}
