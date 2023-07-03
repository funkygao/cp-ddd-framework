package ddd.plus.showcase.wms.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 统一的异常码.
 */
@Getter
@AllArgsConstructor
public enum ExceptionCode {
    TaskCannotPerform("1100"), // i18n in resource bundle with messageId=errorCode
    OperatorDisallowed("1101"),
    InvalidOrderNo("1102"),
    ;

    private String errorCode;

    public String error() {
        return errorCode;
    }
}
