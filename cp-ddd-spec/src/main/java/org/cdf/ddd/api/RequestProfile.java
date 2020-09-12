/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.cdf.ddd.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 统一的API系统入参，注意与业务入参分开.
 */
@ToString
@Getter
@Setter
public class RequestProfile implements Serializable {
    private static final long serialVersionUID = 9532184905327019L;

    /**
     * consumer传递的请求跟踪id，用于跟踪调用链，provider负责链式传递，不会根据它做业务逻辑.
     */
    private String traceId;

    /**
     * 租户id.
     */
    private String tenantId;

    /**
     * 与{@code java.util.Locale}里的概念一致：{language}-{country}.
     * <p>
     * <p>e,g. zh-CN</p>
     * <p>其中，zh是语言(language)，CN是国家(country)</p>
     */
    private String locale;

    /**
     * 时区.
     * <p>
     * <p>e,g. GMT+8</p>
     */
    private String timezone;

    /**
     * 预留的扩展属性.
     * <p>
     * <p>由consumer/provider自行约定扩展属性的内容</p>
     */
    private HashMap<String, String> ext = new HashMap<>();

}
