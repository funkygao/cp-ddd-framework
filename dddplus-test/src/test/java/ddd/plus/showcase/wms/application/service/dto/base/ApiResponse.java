package ddd.plus.showcase.wms.application.service.dto.base;

import lombok.Data;

import java.io.Serializable;

@Data
public class ApiResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final int SUCCESS_CODE = 1000;

    protected Integer code;
    protected String errorMessage;
    protected T payload;

    public static ApiResponse<Void> ofOk() {
        return ofOk(null);
    }

    public static <T> ApiResponse<T> ofOk(T payload) {
        ApiResponse r = new ApiResponse<>();
        r.code = SUCCESS_CODE;
        r.payload = payload;
        return r;
    }

    public static <T> ApiResponse<T> ofFailure(int code, String errorMessage) {
        assert code != SUCCESS_CODE;
        ApiResponse r = new ApiResponse<>();
        r.code = code;
        r.errorMessage = errorMessage;
        return r;
    }

    public boolean isOk() {
        return code.equals(SUCCESS_CODE);
    }
}
