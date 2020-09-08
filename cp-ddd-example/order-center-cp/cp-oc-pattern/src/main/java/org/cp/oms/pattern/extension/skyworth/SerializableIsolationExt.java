package org.cp.oms.pattern.extension.skyworth;

import org.x.cp.ddd.annotation.Extension;
import org.cp.oms.pattern.SkyworthPattern;
import org.cp.oms.spec.ext.ISerializableIsolationExt;
import org.cp.oms.spec.model.IOrderModel;
import org.cp.oms.spec.model.vo.LockEntry;

import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

@Extension(code = SkyworthPattern.CODE, value = "skyworthSerializableIsolationExt")
public class SerializableIsolationExt implements ISerializableIsolationExt {

    @Override
    public LockEntry createLockEntry(@NotNull IOrderModel model) {
        return new LockEntry(model.customerProvidedOrderNo(), 5, TimeUnit.MINUTES);
    }
}
