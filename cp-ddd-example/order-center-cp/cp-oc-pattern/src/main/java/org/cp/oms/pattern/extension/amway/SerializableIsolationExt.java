package org.cp.oms.pattern.extension.amway;

import org.x.cp.ddd.annotation.Extension;
import org.cp.oms.pattern.AmwayPattern;
import org.cp.oms.spec.ext.ISerializableIsolationExt;
import org.cp.oms.spec.model.IOrderModel;
import org.cp.oms.spec.model.vo.LockEntry;

import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

@Extension(code = AmwayPattern.CODE, value = "amwaySerializableIsolationExt")
public class SerializableIsolationExt implements ISerializableIsolationExt {

    @Override
    public LockEntry createLockEntry(@NotNull IOrderModel model) {
        return new LockEntry(model.customerProvidedOrderNo(), 1, TimeUnit.HOURS);
    }
}
