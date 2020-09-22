package org.example.bp.oms.isv.extension;

import org.cdf.ddd.annotation.Extension;
import org.example.bp.oms.isv.IsvPartner;
import org.example.cp.oms.spec.ext.ISerializableIsolationExt;
import org.example.cp.oms.spec.model.IOrderModel;
import org.example.cp.oms.spec.model.vo.LockEntry;

import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

@Extension(code = IsvPartner.CODE, value = "isvSerializableIsolationExt")
public class SerializableIsolationExt implements ISerializableIsolationExt {

    @Override
    public LockEntry createLockEntry(@NotNull IOrderModel model) {
        return new LockEntry(model.customerProvidedOrderNo(), 50, TimeUnit.MINUTES);
    }
}
