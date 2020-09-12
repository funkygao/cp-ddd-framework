package org.example.cp.oms.pattern.extension.hair;

import org.cdf.ddd.annotation.Extension;
import org.example.cp.oms.pattern.HairPattern;
import org.example.cp.oms.spec.ext.ISerializableIsolationExt;
import org.example.cp.oms.spec.model.IOrderModel;
import org.example.cp.oms.spec.model.vo.LockEntry;

import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

@Extension(code = HairPattern.CODE, value = "hairSerializableIsolationExt")
public class SerializableIsolationExt implements ISerializableIsolationExt {

    @Override
    public LockEntry createLockEntry(@NotNull IOrderModel model) {
        return new LockEntry(model.customerProvidedOrderNo(), 1, TimeUnit.HOURS);
    }
}
