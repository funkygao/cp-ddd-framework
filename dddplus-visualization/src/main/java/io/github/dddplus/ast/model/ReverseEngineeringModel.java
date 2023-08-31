/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.model;

import io.github.dddplus.ast.model.dumper.sqlite.SqliteDumper;
import io.github.dddplus.ast.report.*;
import lombok.Getter;

import java.util.*;

/**
 * 逆向模型.
 *
 * <p>模型与程序语言主要的区别不在于图形化，也不在于抽象的程度，而在于表达方式突破了程序设计语言“单一顺序/一维”的限制，模型可以更容易和直接地表达复杂的多维结构.</p>
 * <p>通过AST分析，把代码变成结构化数据.</p>
 */
@Getter
public class ReverseEngineeringModel {
    private AggregateReport aggregateReport = new AggregateReport(this);
    private KeyModelReport keyModelReport = new KeyModelReport(this);
    private List<SimilarityEntry> similarities = new LinkedList<>();
    private List<SimilarityEntry> rawSimilarities = new LinkedList<>();
    private KeyBehaviorReport keyBehaviorReport = new KeyBehaviorReport();
    private KeyFlowReport keyFlowReport = new KeyFlowReport(this);
    private KeyRuleReport keyRuleReport = new KeyRuleReport();
    private KeyEventReport keyEventReport = new KeyEventReport();
    private KeyUsecaseReport keyUsecaseReport = new KeyUsecaseReport();
    private KeyRelationReport keyRelationReport = new KeyRelationReport(this);
    private ClassMethodReport classMethodReport = new ClassMethodReport(this);
    private ClassHierarchyReport classHierarchyReport = new ClassHierarchyReport();
    private ModelDebtReport modelDebtReport = new ModelDebtReport(this);
    private EncapsulationReport encapsulationReport = new EncapsulationReport();
    private Set<String> packages = new TreeSet<>();

    public List<SimilarityEntry> sortedSimilarities() {
        Collections.sort(similarities, Comparator.comparing(SimilarityEntry::getSimilarity));
        return similarities;
    }

    public void registerPackage(String packageName) {
        packages.add(packageName);
    }

    public boolean hasPackage(String packageName) {
        return packages.contains(packageName);
    }

    public ReverseEngineeringModel addSimilarityEntry(SimilarityEntry similarityEntry) {
        similarities.add(similarityEntry);
        return this;
    }

    public List<SimilarityEntry> sortedRawSimilarities() {
        Collections.sort(rawSimilarities, Comparator.comparing(SimilarityEntry::getSimilarity));
        return rawSimilarities;
    }

    public ReverseEngineeringModel addRawSimilarityEntry(SimilarityEntry similarityEntry) {
        rawSimilarities.add(similarityEntry);
        return this;
    }

    public List<AggregateEntry> aggregates() {
        return aggregateReport.getAggregateEntries();
    }

    public List<AggregateEntry> sortedAggregates() {
        List<AggregateEntry> entries = aggregateReport.getAggregateEntries();
        Collections.sort(entries, Comparator.comparing(AggregateEntry::getName));
        return entries;
    }

    public int annotatedModels() {
        return keyModelReport.models() + keyEventReport.size();
    }

    public int annotatedMethods() {
        return keyModelReport.methods() + keyFlowReport.orphanMethods() + keyUsecaseReport.methods();
    }

    public CoverageReport coverageReport() {
        CoverageReport report = new CoverageReport();
        report.setPublicClazzN(classMethodReport.publicClazzN());
        report.setAnnotatedClazzN(annotatedModels());
        report.setPublicMethodN(classMethodReport.publicMethodN());
        report.setAnnotatedMethodN(annotatedMethods());
        report.setPropertyN(classMethodReport.getFieldsN());
        report.setAnnotatedPropertyN(keyModelReport.propertiesN());

        return report;
    }

    public void dump(String sqliteDbFile) throws Exception {
        new SqliteDumper(sqliteDbFile).dump(this);
    }

    /**
     * Orphan means the flows do not belong to any {@link io.github.dddplus.ast.model.KeyModelEntry}
     */
    private List<KeyFlowEntry> orphanFlows() {
        List<KeyFlowEntry> entries = new ArrayList<>();
        for (String actor : keyFlowReport.actors()) {
            if (keyModelReport.containsActor(actor)) {
                // 已经被修正到 KeyModel了，不是孤儿
                continue;
            }

            List<KeyFlowEntry> orphanFlowsOfActor = keyFlowReport.orphanFlowsOfActor(actor);
            entries.addAll(orphanFlowsOfActor);
        }
        return entries;
    }

    public boolean hasProducer(KeyEventEntry entry) {
        if (getKeyModelReport().hasProducer(entry)) {
            return true;
        }

        for (KeyFlowEntry orphanFlow : orphanFlows()) {
            if (orphanFlow.produceEvent() && orphanFlow.getEvents().contains(entry.getClassName())) {
                return true;
            }
        }

        return false;
    }

}
