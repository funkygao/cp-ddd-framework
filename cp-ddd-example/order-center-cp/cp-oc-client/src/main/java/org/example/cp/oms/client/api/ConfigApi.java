package org.example.cp.oms.client.api;

import org.example.cp.oms.client.dto.ConfigRequest;

import javax.validation.constraints.NotNull;

public interface ConfigApi {

    /**
     * 给{@code Partner}发送信号，来远程动态加载.
     *
     * @param request
     */
    void signalPartner(@NotNull ConfigRequest request);
}
