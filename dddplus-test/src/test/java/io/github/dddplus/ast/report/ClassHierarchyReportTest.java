package io.github.dddplus.ast.report;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClassHierarchyReportTest {

    @Test
    void displayRelations() {
        ClassHierarchyReport report = new ClassHierarchyReport();
        report.getIgnoredParentClasses().add("Serializable");
        // B --+
        //     |- A - X - Serializable
        // C --+
        // 1 - 2 - 3
        // 4 - 5
        report.registerExtendsRelation("B", "A");
        report.registerExtendsRelation("C", "A");
        report.registerExtendsRelation("A", "X");
        report.registerExtendsRelation("X", "Serializable");
        report.registerExtendsRelation("1", "2");
        report.registerExtendsRelation("2", "3");
        report.registerExtendsRelation("4", "5");
        Set<ClassHierarchyReport.Pair> pairs = report.displayRelations();
        assertEquals(6, pairs.size());
    }

    @Test
    void test_1_2_3() {
        ClassHierarchyReport report = new ClassHierarchyReport();
        report.registerExtendsRelation("1", "2");
        report.registerExtendsRelation("2", "3");
        Set<ClassHierarchyReport.Pair> pairs = report.displayRelations();
        assertEquals(pairs.size(), 2);
    }

    @Test
    void test_1_2_and_3_2() {
        ClassHierarchyReport report = new ClassHierarchyReport();
        report.registerExtendsRelation("1", "2");
        report.registerExtendsRelation("3", "2");
        Set<ClassHierarchyReport.Pair> pairs = report.displayRelations();
        assertEquals(pairs.size(), 2);
    }

    @Test
    void complexScenario() {
        ClassHierarchyReport report = new ClassHierarchyReport();
        // AllocateStorageEmptyFirstSort -+
        //                                |- AbstractLocatingSort - LocatingSort - Sort
        // AllocateGroupPickZoneSort     -+
        report.registerExtendsRelation("AllocateStorageEmptyFirstSort", "AbstractLocatingSort");
        report.registerExtendsRelation("AllocateGroupPickZoneSort", "AbstractLocatingSort");
        report.registerImplementsRelation("AbstractLocatingSort", "LocatingSort");
        report.registerImplementsRelation("LocatingSort", "Sort");
        Set<ClassHierarchyReport.Pair> pairs = report.displayRelations();
        assertEquals(pairs.size(), 4);
    }

    @Test
    void register_dup() {
        ClassHierarchyReport report = new ClassHierarchyReport();
        // (from, to)相同，但fromJavadoc不同的场景
        // 即：一个接口的两个实现类名称相同，但功能不同
        report.registerImplementsRelation("StrategyIdentityMatcherServiceImpl", "验收策略身份处理器", null, "StrategyIdentityMatcherService");
        report.registerImplementsRelation("StrategyIdentityMatcherServiceImpl", "上架策略身份处理器", null, "StrategyIdentityMatcherService");
        assertEquals(2, report.getRelations().size());
        assertEquals(2, report.displayRelations().size());
    }

    @Test
    void ignoreParents() {
        ClassHierarchyReport report = new ClassHierarchyReport();
        // a - b - c - d - e
        report.registerExtendsRelation("a", "b");
        report.registerExtendsRelation("b", "c");
        report.registerExtendsRelation("c", "d");
        report.registerExtendsRelation("d", "e");
        assertEquals(4, report.displayRelations().size());
        report.getIgnoredParentClasses().add("d");
        // a - b - c
        assertEquals(2, report.displayRelations().size());
        report.getIgnoredParentClasses().clear();
        assertEquals(4, report.displayRelations().size());
        report.getIgnoredParentClasses().add("c");
        assertEquals(2, report.displayRelations().size());
    }

}
