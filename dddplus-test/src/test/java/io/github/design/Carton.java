package io.github.design;

import io.github.dddplus.model.association.BelongTo;
import lombok.Getter;

public class Carton {
    @Getter
    private BelongToCheckTask ownerTask;

    public interface BelongToCheckTask extends BelongTo<CheckTask> {
    }

    private void demo() {
        CheckTask task = this.getOwnerTask().get();
    }
}
