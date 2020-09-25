package org.example.cp.oms.client.api;

import org.cdf.ddd.api.RequestProfile;
import org.example.cp.oms.client.dto.SubmitOrderRequest;

import javax.validation.constraints.NotNull;

public interface SubmitOrderApi {

    /**
     * 统一的下单服务.
     *
     * @param requestProfile     系统入参
     * @param submitOrderRequest 业务入参
     */
    void submit(@NotNull RequestProfile requestProfile, @NotNull SubmitOrderRequest submitOrderRequest);
}
