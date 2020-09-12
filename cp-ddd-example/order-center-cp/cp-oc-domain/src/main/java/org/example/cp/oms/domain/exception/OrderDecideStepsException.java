package org.example.cp.oms.domain.exception;

import org.cdf.ddd.step.IDecideStepsException;

import java.util.List;

public class OrderDecideStepsException extends OrderException implements IDecideStepsException {

    private List<String> subsequentSteps;

    public OrderDecideStepsException withSubsequentSteps(List<String> subsequentSteps) {
        this.subsequentSteps = subsequentSteps;
        return this;
    }

    @Override
    public List<String> subsequentSteps() {
        return this.subsequentSteps;
    }
}
