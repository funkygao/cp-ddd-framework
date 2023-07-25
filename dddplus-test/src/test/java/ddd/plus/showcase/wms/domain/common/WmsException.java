package ddd.plus.showcase.wms.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 一个简单的业务异常定义s.
 */
public class WmsException extends RuntimeException {

    public WmsException(Code code) {

    }

    public WmsException(String exceptionCode) {

    }

    @Getter
    @AllArgsConstructor
    public enum Code {
        TaskCannotPerform("1100"), // i18n in resource bundle with messageId=errorCode
        OperatorDisallowed("1101"),
        InvalidOrderNo("1102"),
        TaskNotFound("1103"),
        ContainerNotFound("1104"),
        ShipNotAllowed("1105"),
        ;

        private String errorCode;
    }

}
