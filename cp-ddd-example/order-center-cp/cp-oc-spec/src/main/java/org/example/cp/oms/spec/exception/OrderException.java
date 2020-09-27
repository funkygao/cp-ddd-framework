package org.example.cp.oms.spec.exception;

import javax.validation.constraints.NotNull;

public class OrderException extends RuntimeException {
    protected OrderErrorSpec errorReason;

    public OrderException(@NotNull OrderErrorSpec errorReason) {
        super();
        this.errorReason = errorReason;
    }

    public String code() {
        return errorReason.code();
    }

    @Override
    public String getMessage() {
        return code();
    }

}
