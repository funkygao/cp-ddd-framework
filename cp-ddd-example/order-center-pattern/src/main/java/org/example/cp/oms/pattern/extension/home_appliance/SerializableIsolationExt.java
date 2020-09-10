package org.example.cp.oms.pattern.extension.home_appliance;

import org.ddd.cp.ddd.annotation.Extension;
import org.example.cp.oms.pattern.HomeAppliancePattern;
import org.example.cp.oms.spec.ext.ISerializableIsolationExt;
import org.example.cp.oms.spec.model.IOrderModel;
import org.example.cp.oms.spec.model.vo.LockEntry;

import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

@Extension(code = HomeAppliancePattern.CODE, value = "homeSerializableIsolationExt")
public class SerializableIsolationExt implements ISerializableIsolationExt {

    @Override
    public LockEntry createLockEntry(@NotNull IOrderModel model) {
        return new LockEntry(model.customerProvidedOrderNo(), 5, TimeUnit.MINUTES);
    }
}
