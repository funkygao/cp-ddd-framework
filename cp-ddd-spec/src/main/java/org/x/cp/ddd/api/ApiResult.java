package org.x.cp.ddd.api;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ApiResult<T> implements Serializable {
    private static final long serialVersionUID = 9532184905327019L;

    private String code;

    private String message;

    private T data;

    /**
     * 预留的扩展属性.
     * <p>
     * <p>由consumer/provider自行约定扩展属性的内容</p>
     */
    private Map<String, String> ext = new HashMap<>();

}
