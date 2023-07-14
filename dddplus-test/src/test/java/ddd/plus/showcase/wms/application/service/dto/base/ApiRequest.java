package ddd.plus.showcase.wms.application.service.dto.base;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public abstract class ApiRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String operatorNo;
    protected String warehouseNo;

    private Map<String, Object> attachments = new HashMap<>();
}
