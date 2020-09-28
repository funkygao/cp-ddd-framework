package org.example.cp.oms.spec.model.vo;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

@ToString
public class LockEntry {

    private final String key;
    private String prefix;

    @Getter
    private final TimeUnit timeUnit;

    @Getter
    private final long leaseTime;

    public LockEntry(@NotNull String key, long leaseTime, @NotNull TimeUnit timeUnit) {
        this.key = key;
        this.timeUnit = timeUnit;
        this.leaseTime = leaseTime;
    }

    public LockEntry withPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String lockKey() {
        if (prefix != null) {
            return prefix + ":" + key;
        }

        return key;
    }

}
