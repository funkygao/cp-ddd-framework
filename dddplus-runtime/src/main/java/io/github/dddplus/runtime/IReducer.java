/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.runtime;

import java.util.List;
import java.util.function.Predicate;

/**
 * 扩展点执行的收敛逻辑，控制扩展点多个实例的叠加.
 * <p>
 * <p>MapReduce pattern.</p>
 * <p>Accepts a list of extensions and a reduce function to produce the result.</p>
 * <p>It basically says the providers can coexist, but you need to coordinate their results.</p>
 *
 * @param <R> 扩展点方法的返回值类型
 */
public interface IReducer<R> {

    /**
     * 扩展点执行结果的收敛.
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

    /**
     * 执行第一个匹配的扩展点.
     *
     * @param predicate 断言，判断是否找到了第一个匹配的扩展点
     * @param <R>       扩展点方法的返回值类型
     * @return 扩展点返回值
     */
    static <R> IReducer<R> firstOf(final Predicate<R> predicate) {
        return new IReducer<R>() {
            @Override
            public R reduce(List<R> accumulatedResults) {
                R result = null;
                if (accumulatedResults != null && !accumulatedResults.isEmpty()) {
                    // 此时accumulatedResults只会有一个元素
                    result = accumulatedResults.get(0);
                }

                return result;
            }

            @Override
            public boolean shouldStop(List<R> accumulatedResults) {
                boolean result = false;
                if (accumulatedResults != null && !accumulatedResults.isEmpty()) {
                    result = predicate.test(accumulatedResults.get(accumulatedResults.size() - 1));
                }
                return result;
            }
        };
    }

    /**
     * 执行所有的扩展点，并获取期望的结果.
     *
     * @param predicate expected result predicate. if null, always return null
     * @param <R>       扩展点方法的返回值类型
     * @return the value that satisfies predicate. if none satisfied, returns null
     */
    static <R> IReducer<R> allOf(Predicate<R> predicate) {
        return new IReducer<R>() {
            @Override
            public R reduce(List<R> accumulatedResults) {
                if (predicate == null) {
                    return null;
                }

                for (R r : accumulatedResults) {
                    if (predicate.test(r)) {
                        return r;
                    }
                }

                return null;
            }

            @Override
            public boolean shouldStop(List<R> accumulatedResults) {
                return false;
            }
        };
    }
}
