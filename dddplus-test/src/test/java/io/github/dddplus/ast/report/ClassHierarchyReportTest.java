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
        assertEquals(5, pairs.size());
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

}
