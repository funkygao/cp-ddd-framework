/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.model;

/**
 * Unit of Work，解决跨聚合根的事务问题.
 *
 * <p>架构分层上它属于Application Layer.</p>
 * <p>According to M. Fowler, the UoW is "just" a smart persistence tool，严格意义上与其相符的是{@code DirtyMemento}.</p>
 * <p></p>
 * @see <a href="http://martinfowler.com/eaaCatalog/unitOfWork.html">Martin Fowler解释UoW</a>
 */
public interface IUnitOfWork {
}
