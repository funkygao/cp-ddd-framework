package org.x.cp.ddd.runtime;

import java.util.List;

/**
 * 扩展点执行的归约器，控制扩展点多个实例的叠加.
 *
 * @param <R> 扩展点方法的返回值类型
 */
public interface IReducer<R> {

    /**
     * 扩展点执行结果归约.
     *
     * @param accumulatedResults 目前已经执行的所有扩展点的结果集
     * @return 计算后的扩展点结果
     */
    R reduce(List<R> accumulatedResults);

    /**
     * 判断扩展点执行是否该停下来.
     *
     * @param accumulatedResults 目前已经执行的所有扩展点的结果集
     * @return 是否应该停下后续扩展点的执行
     */
    boolean shouldStop(List<R> accumulatedResults);
}
