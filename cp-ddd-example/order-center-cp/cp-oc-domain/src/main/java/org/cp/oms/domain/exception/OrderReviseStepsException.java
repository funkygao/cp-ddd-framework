package org.cp.oms.domain.exception;

import org.x.cp.ddd.model.IReviseStepsException;

import java.util.List;

public class OrderReviseStepsException extends OrderException implements IReviseStepsException {

    private List<String> subsequentSteps;

    public OrderReviseStepsException withSubsequentSteps(List<String> subsequentSteps) {
        this.subsequentSteps = subsequentSteps;
        return this;
    }

    @Override
    public List<String> subsequentSteps() {
        return this.subsequentSteps;
    }
}
