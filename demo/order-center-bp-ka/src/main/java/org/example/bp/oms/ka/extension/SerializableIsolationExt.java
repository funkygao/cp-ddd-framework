package org.example.bp.oms.ka.extension;

import lombok.extern.slf4j.Slf4j;
import io.github.dddplus.annotation.Extension;
import org.example.cp.oms.spec.ext.ISerializableIsolationExt;
import org.example.cp.oms.spec.model.IOrderModel;
import org.example.cp.oms.spec.model.vo.LockEntry;
import org.example.bp.oms.ka.KaPartner;

import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

@Extension(code = KaPartner.CODE, value = "kaSerializableIsolationExt")
@Slf4j
public class SerializableIsolationExt implements ISerializableIsolationExt {

    @Override
    public LockEntry createLockEntry(@NotNull IOrderModel model) {
        log.info("KA的锁TTL大一些");
        return new LockEntry(model.customerProvidedOrderNo(), 11, TimeUnit.MINUTES);
    }
}
