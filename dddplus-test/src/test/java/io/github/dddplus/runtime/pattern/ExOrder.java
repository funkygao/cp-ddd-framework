package io.github.dddplus.runtime.pattern;

import io.github.dddplus.ext.IIdentity;
import lombok.Data;

@Data
public class ExOrder implements IIdentity {
    Integer moneyCollected;
}
