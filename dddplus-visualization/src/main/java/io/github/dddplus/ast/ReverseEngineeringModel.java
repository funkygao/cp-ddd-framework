package io.github.dddplus.ast;

import io.github.dddplus.ast.model.AggregateEntry;
import io.github.dddplus.ast.model.KeyEventEntry;
import io.github.dddplus.ast.model.KeyFlowEntry;
import io.github.dddplus.ast.model.SimilarityEntry;
import io.github.dddplus.ast.report.*;
import lombok.Getter;

import java.util.*;

/**
 * 逆向模型.
 *
 * <p>模型与程序语言主要的区别不在于图形化，也不在于抽象的程度，而在于表达方式突破了程序设计语言“单一顺序/一维”的限制，模型可以更容易和直接地表达复杂的多维结构.</p>
 * <pre>
 * RelationEntry
 * KeyEventEntry
 * AggregateEntry
 *     KeyElementEntry
 *         KeyBehaviorEntry
 *         KeyRuleEntry
 *         KeyFlowEntry
 * </pre>
 */
@Getter
public class ReverseEngineeringModel {
    private AggregateReport aggregateReport = new AggregateReport();
    private KeyModelReport keyModelReport = new KeyModelReport(this);
    private List<SimilarityEntry> similarities = new LinkedList<>();
    private List<SimilarityEntry> rawSimilarities = new LinkedList<>();
    private KeyBehaviorReport keyBehaviorReport = new KeyBehaviorReport();
    private KeyFlowReport keyFlowReport = new KeyFlowReport();
    private KeyRuleReport keyRuleReport = new KeyRuleReport();
    private KeyEventReport keyEventReport = new KeyEventReport();
    private KeyUsecaseReport keyUsecaseReport = new KeyUsecaseReport();
    private KeyRelationReport keyRelationReport = new KeyRelationReport(this);
    private ClassMethodReport classMethodReport = new ClassMethodReport(this);
    private ModelDebtReport modelDebtReport = new ModelDebtReport(this);


    public List<SimilarityEntry> sortedSimilarities() {
        Collections.sort(similarities, Comparator.comparing(SimilarityEntry::getSimilarity));
        return similarities;
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
