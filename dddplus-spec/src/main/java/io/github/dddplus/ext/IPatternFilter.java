/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ext;

import io.github.dddplus.model.IBag;
import lombok.NonNull;

/**
 * {@code Pattern}的集合对象过滤器.
 *
 * <p>业务模式被识别后，它可能需要从集合对象中过滤出它相关的子集，此时，{@code Pattern}可以实现{@link IPatternFilter}完成该功能</p>
 */
public interface IPatternFilter {

    /**
     * 对集合对象进行过滤.
     *
     * @param bag 集合对象
     * @return 过滤后的集合对象：子集
     */
    IBag filter(@NonNull IBag bag);
}
