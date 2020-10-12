package org.example.bp.oms.isv;

import lombok.extern.slf4j.Slf4j;
import org.cdf.ddd.plugin.IContainerContext;
import org.cdf.ddd.plugin.IPluginListener;

@Slf4j
public class PluginListener implements IPluginListener {

    @Override
    public void onCommitted(IContainerContext ctx) throws Exception {
        log.info("ISV Jar loaded, ctx:{}", ctx);
    }

    @Override
    public void onPrepared(IContainerContext ctx) throws Exception {

    }
}
