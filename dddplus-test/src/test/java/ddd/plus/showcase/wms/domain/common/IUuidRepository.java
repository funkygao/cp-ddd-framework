package ddd.plus.showcase.wms.domain.common;

import io.github.dddplus.model.IRepository;

public interface IUuidRepository extends IRepository {

    /**
     * 独立事务，此时业务单号可能尚未生成.
     *
     * <p>db.uuid.(uuid, type) is unique key</p>
     */
    @Transactional
    boolean tryInsert(Uuid uuid);

    /**
     * 非独立事务，更新业务单号信息，以便重复请求可以拿到之前的业务数据.
     *
     * <p>如果{@link #tryInsert(Uuid)}时业务单号已经有了，此步骤可忽略</p>
     */
    void update(Uuid uuid);

}
