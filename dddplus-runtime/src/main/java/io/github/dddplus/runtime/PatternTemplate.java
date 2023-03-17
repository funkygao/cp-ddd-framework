/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.runtime;

import io.github.dddplus.ext.IIdentityResolver;
import io.github.dddplus.model.IDomainModel;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;

/**
 * 业务模式身份基础解析器.
 * <p>通过模板方法，实现不同领域模型的match方法动态分发</p>
 * <p>IMPORTANT: 子类的每个match方法入参必须是{@link IDomainModel}</p>
 */
public abstract class PatternTemplate implements IIdentityResolver<IDomainModel> {
    private static final String MATCH_METHOD_NAME = "match";

    @Override
    public final boolean match(@NotNull IDomainModel model) {
        // TODO cache and exception handling
        try {
            Class<? extends IDomainModel> modelClazz = model.getClass();
            Method actualMatchMethod = modelClazz.getMethod(MATCH_METHOD_NAME, modelClazz);
            actualMatchMethod.setAccessible(true);
            return (Boolean) actualMatchMethod.invoke(this, model);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

}
