package io.github.dddplus.ast.model;

import lombok.Data;

import java.util.Set;

@Data
public class AccessorsEntry {
    private String className;
    private String methodName;
    private Set<String> accessorsClasses;

    public boolean satisfy(String accessorClazz) {
        return accessorsClasses.contains(accessorClazz);
    }
}
