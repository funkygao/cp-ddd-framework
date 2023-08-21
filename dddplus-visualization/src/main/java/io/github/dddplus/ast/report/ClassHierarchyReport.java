package io.github.dddplus.ast.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.*;

@Getter
public class ClassHierarchyReport {
    private List<String> ignoredParentClasses = new ArrayList<>();

    private Set<Pair> extendsRelations = new HashSet<>();
    private Set<Pair> implementsRelations = new HashSet<>();

    public boolean ignoreParentClass(String parentClass) {
        return ignoredParentClasses.contains(parentClass);
    }

    public void registerExtendsRelation(String from, String fromJavadoc, String to) {
        extendsRelations.add(new Pair(from, fromJavadoc, to));
    }

    public void registerImplementsRelation(String from, String fromJavadoc, String to) {
        implementsRelations.add(new Pair(from, fromJavadoc, to));
    }

    public Set<Pair> extendsRelations() {
        return relationsOf(extendsRelations);
    }

    public Set<Pair> implementsRelations() {
        return relationsOf(implementsRelations);
    }

    private Set<Pair> relationsOf(Set<Pair> relations) {
        Set<Pair> result = new HashSet<>();
        for (Pair pair : relations) {
            // find pairs of multiple 'from' to a 'to'
            for (Pair pair1 : relations) {
                if (ignoreParentClass(pair.getTo())) {
                    continue;
                }

                if (pair.equals(pair1)) {
                    continue;
                }

                if (pair.getTo().equals(pair1.getTo())) {
                    result.add(pair);
                }
            }
        }

        // B --+
        //     |- A - X
        // C --+
        return result;
    }

    @Data
    @AllArgsConstructor
    public static class Pair {
        private String from;
        private String fromJavadoc;
        private String to;
    }

}
