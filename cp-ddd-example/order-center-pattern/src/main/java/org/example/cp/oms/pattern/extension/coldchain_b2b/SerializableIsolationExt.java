package org.example.cp.oms.pattern.extension.coldchain_b2b;

import org.cdf.ddd.annotation.Extension;
import org.example.cp.oms.pattern.ColdChainB2BPattern;
import org.example.cp.oms.spec.ext.ISerializableIsolationExt;
import org.example.cp.oms.spec.model.IOrderModel;
import org.example.cp.oms.spec.model.vo.LockEntry;

import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

@Extension(code = ColdChainB2BPattern.CODE, value = "ccb2bSerializableIsolationExt")
public class SerializableIsolationExt implements ISerializableIsolationExt {

    @Override
    public LockEntry createLockEntry(@NotNull IOrderModel model) {
        return new LockEntry(model.customerProvidedOrderNo(), 19, TimeUnit.MINUTES);
    }
}
