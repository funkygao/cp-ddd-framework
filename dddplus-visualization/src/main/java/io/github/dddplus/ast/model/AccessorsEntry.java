package io.github.dddplus.ast.model;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import io.github.dddplus.ast.parser.JavaParserUtil;
import lombok.Data;

import java.util.Set;

@Data
public class AccessorsEntry {
    private String declaredClassName;
    private String declaredMethodName;
    private Set<String> accessorsClasses;

    public boolean satisfy(ClassOrInterfaceDeclaration accessorClazz) {
        for (String registeredAccessor : accessorsClasses) {
            if (JavaParserUtil.implementsInterface(accessorClazz, registeredAccessor)) {
                return true;
            }
        }

        return false;
    }
}
