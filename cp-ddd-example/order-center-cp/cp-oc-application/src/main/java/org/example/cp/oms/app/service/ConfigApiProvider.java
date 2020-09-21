package org.example.cp.oms.app.service;

import lombok.extern.slf4j.Slf4j;
import org.cdf.ddd.runtime.DDD;
import org.example.cp.oms.client.api.ConfigApi;
import org.example.cp.oms.client.dto.ConfigRequest;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
@Slf4j
public class ConfigApiProvider implements ConfigApi {

    @Override
    public void signalPartner(@NotNull ConfigRequest request) {
        // 先卸载
        DDD.getContainer().unloadPartnerPlugin(request.getPartnerCode());

        // 再加载
        try {
            DDD.getContainer().loadPartnerPlugin(request.getJarURL().toString(), "org.example.bp.oms");
        } catch (Exception ex) {
            log.error("load:{}", request, ex);
        }
    }

}
