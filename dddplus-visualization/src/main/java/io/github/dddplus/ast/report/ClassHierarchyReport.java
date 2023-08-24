package io.github.dddplus.ast.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.*;

@Getter
public class ClassHierarchyReport {
    private List<String> ignoredParentClasses = new ArrayList<>();

    private Set<Pair> relations = new HashSet<>();

    private boolean ignored(Pair pair) {
        return ignoredParentClasses.contains(pair.from) || ignoredParentClasses.contains(pair.to);
    }

    public void registerExtendsRelation(String from, String fromJavadoc, List<String> genericTypes, String to) {
        Pair pair = new Pair(this, from, to, Pair.Relation.Extends, fromJavadoc, genericTypes);
        if (!ignored(pair)) {
            relations.add(pair);
        }
    }

    void registerExtendsRelation(String from, String to) {
        registerExtendsRelation(from, "", null, to);
    }

    public void registerImplementsRelation(String from, String fromJavadoc, List<String> genericTypes, String to) {
        Pair pair = new Pair(this, from, to, Pair.Relation.Implements, fromJavadoc, genericTypes);
        if (!ignored(pair)) {
            relations.add(pair);
        }
    }

    public Set<Pair> displayRelations() {
        // 删除只有一层关系的Pair
        Set<Pair> result = new TreeSet<>();
        for (Pair self : relations) {
            if (ignored(self)) {
                continue;
            }

            for (Pair that : relations) {
                if (that == self || ignored(that)) {
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
        private ClassHierarchyReport report;
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

        public String dotFrom() {
            String dotLabel = dotLabel();
            if (dotLabel.isEmpty()) {
                return from;
            }

            return from + "\n" + dotLabel;
        }

        public String dotTo() {
            for (Pair pair : report.relations) {
                if (to.equals(pair.from)) {
                    return pair.dotFrom();
                }
            }

            return to;
        }

        private String dotLabel() {
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
