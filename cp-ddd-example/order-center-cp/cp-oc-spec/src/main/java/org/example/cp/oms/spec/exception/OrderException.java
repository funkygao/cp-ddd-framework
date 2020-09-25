package org.example.cp.oms.spec.exception;

import lombok.Getter;

public class OrderException extends RuntimeException {

    /**
     * 异常码.
     */
    @Getter
    private final String code;

    public OrderException(String code) {
        super(code);
        this.code = code;
    }

}
