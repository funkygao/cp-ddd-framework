package org.example.bp.oms.ka.extension;

import org.cdf.ddd.annotation.Extension;
import org.example.cp.oms.spec.ext.ISerializableIsolationExt;
import org.example.cp.oms.spec.model.IOrderModel;
import org.example.cp.oms.spec.model.vo.LockEntry;
import org.example.cp.oms.spec.partner.KaPartner;

import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

@Extension(code = KaPartner.CODE, value = "kaSerializableIsolationExt")
public class SerializableIsolationExt implements ISerializableIsolationExt {

    @Override
    public LockEntry createLockEntry(@NotNull IOrderModel model) {
        return new LockEntry(model.customerProvidedOrderNo(), 11, TimeUnit.MINUTES);
    }
}
