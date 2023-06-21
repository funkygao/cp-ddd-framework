package io.github.dddplus.runtime.pattern;

import io.github.dddplus.model.IIdentity;
import lombok.Data;

@Data
public class ExOrder implements IIdentity {
    Integer moneyCollected;
}
