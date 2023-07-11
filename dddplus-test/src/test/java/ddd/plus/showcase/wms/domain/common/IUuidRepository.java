package ddd.plus.showcase.wms.domain.common;

import io.github.dddplus.model.IRepository;
import org.springframework.dao.DuplicateKeyException;

public interface IUuidRepository extends IRepository {

    // 如果存在，会把数据库信息拉到uuid.bizNo
    boolean exists(Uuid uuid);

    /**
     * 靠unique key实现.
     *
     * @see DuplicateKeyException
     */
    void insert(Uuid uuid);

}
