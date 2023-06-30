package ddd.plus.showcase.wms.domain.task.dict;

public enum ContainerType {
    /**
     * 虚拟容器.
     *
     * <p>物理世界里并不存在，它只是个编号.</p>
     * <p>无需 hold/release 操作.</p>
     */
    Virtual,

    /**
     * 物理世界里真实存在的并且确实是容纳货品的载体.
     */
    Physical;
}
