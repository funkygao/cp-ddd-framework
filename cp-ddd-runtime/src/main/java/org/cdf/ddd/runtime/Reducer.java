/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.cdf.ddd.runtime;

import java.util.List;
import java.util.function.Predicate;

/**
 * 常用的扩展点执行的归约器实现.
 *
 * @param <R> 扩展点方法的返回值类型
 */
public abstract class Reducer<R> implements IReducer<R> {

    /**
     * 执行第一个匹配的扩展点.
     *
     * @param predicate 断言，判断是否找到了第一个匹配的扩展点
     * @param <R>       扩展点方法的返回值类型
     * @return 扩展点返回值
     */
    public static <R> Reducer<R> firstOf(final Predicate<R> predicate) {
        return new Reducer<R>() {
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
     * 执行所有的扩展点.
     *
     * @param <R> 扩展点方法的返回值类型
     * @return will always be null
     */
    public static <R> Reducer<R> all() {
        return new Reducer<R>() {
            @Override
            public R reduce(List<R> accumulatedResults) {
                // 返回的归约结果，是null
                return null;
            }

            @Override
            public boolean shouldStop(List<R> accumulatedResults) {
                return false;
            }
        };
    }

}
