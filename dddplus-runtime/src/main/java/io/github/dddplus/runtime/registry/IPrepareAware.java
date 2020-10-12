/*
 * Copyright cp-ddd-framework Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.runtime.registry;

import javax.validation.constraints.NotNull;

interface IPrepareAware {

    void prepare(@NotNull Object bean);
}
