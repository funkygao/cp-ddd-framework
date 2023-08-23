package io.github.dddplus.ast.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.*;

@Getter
public class ClassHierarchyReport {
    private List<String> ignoredParentClasses = new ArrayList<>();

    private Set<Pair> relations = new HashSet<>();

    private boolean ignoreParentClass(String parentClass) {
        return ignoredParentClasses.contains(parentClass);
    }

    public void registerExtendsRelation(String from, String fromJavadoc, List<String> genericTypes, String to) {
        if (ignoreParentClass(to)) {
            return;
        }

        relations.add(new Pair(from, to, Pair.Relation.Extends, fromJavadoc, genericTypes));
    }

    void registerExtendsRelation(String from, String to) {
        registerExtendsRelation(from, "", null, to);
    }

    public void registerImplementsRelation(String from, String fromJavadoc, List<String> genericTypes, String to) {
        if (ignoreParentClass(to)) {
            return;
        }

        relations.add(new Pair(from, to, Pair.Relation.Implements, fromJavadoc, genericTypes));
    }

    public Set<Pair> displayRelations() {
        // 删除只有一层关系的Pair
        Set<Pair> result = new TreeSet<>();
        for (Pair self : relations) {
            if (ignoreParentClass(self.to)) {
                continue;
            }

            for (Pair that : relations) {
                if (that == self || ignoreParentClass(that.to)) {
                    continue;
                }

                if (that.to.equals(self.from) || that.from.equals(self.to) || that.to.equals(self.to)) {
                    // this link has neighbor
                    result.add(self);
                }
            }
        }
        return result;
    }

    void registerImplementsRelation(String from, String to) {
        registerImplementsRelation(from, "", null, to);
    }

    @Data
    @AllArgsConstructor
    public static class Pair implements Comparable<Pair> {
        private String from;
        private String to;
        private Relation relation;
        private String fromJavadoc;
        // 这个关系确定了哪些泛型类型
        private List<String> genericTypes;

        private String displayGenericTypes() {
            if (genericTypes == null || genericTypes.isEmpty()) {
                return "";
            }

            return String.join(",", genericTypes);
        }

        public String dotLabel() {
            String javadoc = fromJavadoc.replaceAll("@", "")
                    .replaceAll("\"", "");
            String displayGenericTypes = displayGenericTypes();
            if (displayGenericTypes.isEmpty()) {
                return javadoc;
            }

            return javadoc + " <" + displayGenericTypes + ">";
        }

        @Override
        public int compareTo(Pair that) {
            int result = to.compareTo(that.to);
            if (result != 0) {
                return result;
            }
            result = from.compareTo(that.from);
            if (result != 0) {
                return result;
            }
            
            return fromJavadoc.compareTo(that.fromJavadoc);
        }

        public enum Relation {
            Extends,
            Implements;
        }
    }

}
