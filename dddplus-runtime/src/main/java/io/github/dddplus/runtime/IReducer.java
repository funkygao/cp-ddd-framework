/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.runtime;

import lombok.NonNull;

import java.util.List;
import java.util.function.Predicate;

/**
 * 扩展点执行链(多个实例叠加，串行链式调用)的收敛逻辑.
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
     * 执行所有的扩展点，直到谓词第一次匹配到了扩展点返回值为止.
     *
     * <p>典型场景：是否允许用户注册是扩展点，注册时无需知道有哪些扩展点实现，逐一执行，直到找到一个返回false就停下来，Fail Fast</p>
     * <p>与{@code firstExtension}不同，找到第一个符合业务身份的扩展点 vs 逐一执行符合业务身份扩展点直到第一个结果符合谓词条件</p>
     *
     * @param predicate 谓词，判断扩展点返回值是否符合谓词条件
     * @param <R>       扩展点方法的返回值类型
     * @return 符合谓词条件的那一个扩展点返回值；如果没找到谓词匹配，则返回最后那一个扩展点执行结果
     */
    static <R> IReducer<R> stopOnFirstMatch(@NonNull final Predicate<R> predicate) {
        return new IReducer<R>() {
            @Override
            public R reduce(List<R> accumulatedResults) {
                R tail = tail(accumulatedResults);
                if (tail == null) {
                    return null;
                }

                return tail;
            }

            @Override
            public boolean shouldStop(List<R> accumulatedResults) {
                R tail = tail(accumulatedResults);
                if (tail == null) {
                    return false;
                }

                return predicate.test(tail);
            }

            private R tail(List<R> accumulatedResults) {
                if (accumulatedResults == null || accumulatedResults.isEmpty()) {
                    return null;
                }

                return accumulatedResults.get(accumulatedResults.size() - 1);
            }
        };
    }

    /**
     * 执行所有的扩展点，并永远返回null.
     *
     * @param <R> 扩展点方法的返回值类型
     * @return always null
     */
    static <R> IReducer<R> allOf() {
        return new IReducer<R>() {
            @Override
            public R reduce(List<R> accumulatedResults) {
                return null;
            }

            @Override
            public boolean shouldStop(List<R> accumulatedResults) {
                // never stop
                return false;
            }
        };
    }

    /**
     * 执行所有的扩展点，并获取满足谓词条件的结果.
     *
     * @param predicate expected result predicate. if null, always return null
     * @param <R>       扩展点方法的返回值类型
     * @return the value that satisfies predicate. if none satisfied, returns null
     * @deprecated 这个方法理解起来怪怪的，use {@link #allOf()} instead
     */
    @Deprecated
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
