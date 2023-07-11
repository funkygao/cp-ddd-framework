package ddd.plus.showcase.wms.domain.common;

import io.github.dddplus.model.AbstractBusinessNo;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.dao.DuplicateKeyException;

/**
 * 通用的业务幂等对象.
 *
 * <ul>重复请求，要满足：
 * <li>对外：返回相同结果</li>
 * <li>对内：自身状态不再发生改变</li>
 * </ul>
 */
@Getter
public class Uuid extends AbstractBusinessNo<String> {
    public enum Type {
        Task,
        Carton,
        Order,
        // ...
        ;
    }

    @Setter
    private String bizNo;
    private Type type;
    private WarehouseNo warehouseNo;

    private IUuidRepository repository;

    protected Uuid(@NonNull String value) {
        super(value);
    }

    public static Uuid of(@NonNull String uuidNo, Type type, WarehouseNo warehouseNo, IUuidRepository uuidRepository) {
        Uuid uuid = new Uuid(uuidNo);
        uuid.type = type;
        uuid.warehouseNo = warehouseNo;
        uuid.repository = uuidRepository;
        return uuid;
    }

    public boolean exists() {
        return repository.exists(this);
    }

    /**
     * 确保自身状态改变只发生一次.
     */
    public boolean assureVaryOnce() {
        try {
            repository.insert(this);
            return true;
        } catch (DuplicateKeyException ignored) {
            return false;
        }
    }
}
