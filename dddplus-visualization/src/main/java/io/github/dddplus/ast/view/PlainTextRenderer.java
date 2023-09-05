/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.view;

import io.github.dddplus.ast.model.*;
import io.github.dddplus.ast.parser.JavaParserUtil;
import io.github.dddplus.ast.report.AggregateDensity;
import io.github.dddplus.ast.report.CoverageReport;
import io.github.dddplus.ast.report.ModelDebtReport;
import io.github.dddplus.dsl.KeyElement;

import java.io.IOException;
import java.util.List;

/**
 * DSL -> Reverse Engineering Model -> Plain text file, git versioned.
 */
public class PlainTextRenderer implements IModelRenderer<PlainTextRenderer> {
    private String targetFilename;

    private boolean clustering = false;
    private boolean showRawSimilarities = false;

    private final StringBuilder content = new StringBuilder();
    private ReverseEngineeringModel model;

    public PlainTextRenderer clustering() {
        this.clustering = true;
        return this;
    }

    public PlainTextRenderer targetFilename(String targetFilename) {
        this.targetFilename = targetFilename;
        return this;
    }

    @Override
    public PlainTextRenderer withModel(ReverseEngineeringModel model) {
        this.model = model;
        return this;
    }

    private void addRawModelSimilarities() {
        append("<<相似类>>").append(NEWLINE);
        for (SimilarityEntry entry : model.sortedRawSimilarities()) {
            append(entry.getLeftClass()).append(SPACE).append(entry.getRightClass()).append(SPACE);
            append(String.format("%.0f", entry.getSimilarity())).append(NEWLINE);
        }
    }

    @Override
    public void render() throws IOException {
        appendCoverage();
        appendModelDebt();
        model.sortedAggregates().forEach(a -> addAggregate(a));
        addKeyUsecases();
        addOrphanKeyFlows();
        addKeyEvents();
        if (showRawSimilarities) {
            addRawModelSimilarities();
        }

        JavaParserUtil.dumpToFile(targetFilename, content.toString());
    }

    private PlainTextRenderer appendModelDebt() {
        ModelDebtReport report = model.getModelDebtReport().build();
        append("模型债：");
        AggregateDensity density = report.getAggregateDensity();
        if (report.getProblematicalFields() > 0) {
            append(String.format("问题字段:%d ", report.getProblematicalFields()));
        }
        if (report.getOrphanFlows() > 0) {
            append(String.format("孤儿流程:%d ", report.getOrphanFlows()));
        }
        if (report.getRawSimilarModels() > 0) {
            append(String.format("相似类:%d ", report.getRawSimilarModels()));
        }
        append(String.format("聚合分布[问题聚合:%d 类:(Mean:%.2f SD:%.2f) 方法:(Mean:%.2f SD:%.2f)]",
                density.getProblems(),
                density.getModelsMean(),
                density.getModelsStandardDeviation(),
                density.getMethodDensityMean(),
                density.getMethodDensityStandardDeviation()));
        append(NEWLINE);
        return this;
    }

    private PlainTextRenderer appendCoverage() {
        CoverageReport report = model.coverageReport();
        append("标注覆盖率：")
                .append(String.format("Class(%d/%.1f%%)", report.getPublicClazzN(), report.clazzCoverage()))
                .append(SPACE)
                .append(String.format("Method(%d/%.1f%%)", report.getPublicMethodN(), report.methodCoverage()))
                .append(SPACE)
                .append(String.format("Property(%d/%.1f%%)", report.getPropertyN(), report.propertyCoverage()))
                .append(SPACE)
                .append(String.format("Statement(%d)", model.getClassMethodReport().getStatementN()))
                .append(NEWLINE);
        append("大方法Top10").append(NEWLINE);
        for (Integer loc : model.getClassMethodReport().topTenBigMethods().keySet()) {
            append(TAB).append(model.getClassMethodReport().getMethodInfo().getBigMethods().get(loc))
                    .append(" ").append(String.valueOf(loc)).append(NEWLINE);
        }
        return this;
    }

    private PlainTextRenderer addAggregate(AggregateEntry aggregate) {
        append("<<Aggregate: ").append(aggregate.getName()).append(">>").append(NEWLINE);
        for (KeyModelEntry clazz : aggregate.keyModels()) {
            writeClazzDefinition(clazz);
        }
        return this;
    }

    private PlainTextRenderer writeClazzDefinition(KeyModelEntry keyModelEntry) {
        append(keyModelEntry.getClassName());
        if (keyModelEntry.hasJavadoc()) {
            append(SPACE).append(keyModelEntry.getJavadoc());
        }
        append(NEWLINE);

        if (!keyModelEntry.types().isEmpty()) {
            append(TAB).append("[属性]").append(NEWLINE);
            for (KeyElement.Type type : keyModelEntry.types()) {
                append(TAB)
                        .append(String.format("%-13s %s", type, keyModelEntry.displayFieldByType(type)))
                        .append(NEWLINE);
            }
        }

        if (!keyModelEntry.getKeyBehaviorEntries().isEmpty()) {
            append(TAB).append("[行为]").append(NEWLINE);
            for (KeyBehaviorEntry entry : keyModelEntry.getKeyBehaviorEntries()) {
                append(TAB).append(TAB)
                        .append(entry.displayNameWithRemark())
                        .append("(")
                        .append(entry.displayArgs())
                        .append(")")
                        .append(SPACE)
                        .append(entry.getJavadoc())
                        .append(NEWLINE);
            }

            if (!keyModelEntry.getKeyRuleEntries().isEmpty()) {
                append(TAB).append("[规则]").append(NEWLINE);
                for (KeyRuleEntry entry : keyModelEntry.getKeyRuleEntries()) {
                    append(TAB).append(TAB)
                            .append(entry.displayNameWithRemark())
                            .append("(")
                            .append(entry.displayRefer())
                            .append(")")
                            .append(SPACE)
                            .append(entry.getJavadoc())
                            .append(NEWLINE);
                }
            }

            if (!keyModelEntry.getKeyFlowEntries().isEmpty()) {
                append(TAB).append("[流程]").append(NEWLINE);
                for (KeyFlowEntry entry : keyModelEntry.getKeyFlowEntries()) {
                    append(TAB).append(TAB)
                            .append(entry.getMethodName())
                            .append("(")
                            .append(entry.displayEffectiveArgs())
                            .append(")")
                            .append(SPACE)
                            .append(entry.getJavadoc())
                            .append(SPACE)
                            .append(entry.plainDisplayActualClass());
                    if (entry.produceEvent()) {
                        append(String.format(" -> %s", entry.displayEvents()));
                    }
                    append(NEWLINE);
                }
            }

            if (clustering) {
                List<List<String>> clusters = keyModelEntry.methodClusters();
                if (clusters != null) {
                    append(TAB).append("[聚类]").append(NEWLINE);
                    for (int i = 0; i < clusters.size(); i++) {
                        if (clusters.get(i).isEmpty()) {
                            continue;
                        }
                        append(TAB).append(TAB).append(clusters.get(i).toString()).append(NEWLINE);
                    }
                }
            }
        }

        return this;
    }

    private PlainTextRenderer writeKeyUsecaseClazzDefinition(String actor) {
        append(actor).append(NEWLINE);
        for (KeyUsecaseEntry entry : model.getKeyUsecaseReport().sortedActorKeyUsecases(actor)) {
            append(TAB);
            if (!entry.displayOut().isEmpty()) {
                append(entry.displayOut()).append(SPACE);
            }

            if (entry.isConsumer()) {
                append("on ").append(entry.getKeyEvent());
            } else {
                append(entry.displayNameWithRemark())
                        .append("(")
                        .append(entry.displayIn())
                        .append(")");
            }
            if (entry.hasJavadoc()) {
                append(SPACE).append(entry.getJavadoc());
            }
            append(NEWLINE);
        }
        return this;
    }

    public PlainTextRenderer showRawSimilarities() {
        this.showRawSimilarities = true;
        return this;
    }

    private PlainTextRenderer addKeyUsecases() {
        append("<<用户交互>>").append(NEWLINE);
        for (String actor : model.getKeyUsecaseReport().getData().keySet()) {
            writeKeyUsecaseClazzDefinition(actor);
        }
        return this;
    }

    private PlainTextRenderer addOrphanKeyFlows() {
        append("<<跨聚合复杂流程>>").append(NEWLINE);
        for (String actor : model.getKeyFlowReport().actors()) {
            writeOrphanFlowClazzDefinition(actor);
        }
        return this;
    }

    private PlainTextRenderer writeOrphanFlowClazzDefinition(String actor) {
        if (model.getKeyModelReport().containsActor(actor)) {
            return this;
        }

        List<KeyFlowEntry> orphanFlowsOfActor = model.getKeyFlowReport().orphanFlowsOfActor(actor);
        if (orphanFlowsOfActor.isEmpty()) {
            return this;
        }

        append(actor).append(NEWLINE);
        for (KeyFlowEntry entry : orphanFlowsOfActor) {
            append(TAB).append(entry.displayNameWithRemark())
                    .append("(")
                    .append(entry.displayEffectiveArgs())
                    .append(")")
                    .append(SPACE)
                    .append(entry.getJavadoc());
            if (entry.produceEvent()) {
                append(" -> ").append(entry.displayEvents());
            }
            append(NEWLINE);
        }
        return this;
    }

    private PlainTextRenderer addKeyEvents() {
        content.append("<<领域事件>>").append(NEWLINE);
        for (KeyEventEntry entry : model.getKeyEventReport().sortedEvents()) {
            append(TAB).writeClazzDefinition(entry);
        }
        return this;
    }

    private PlainTextRenderer writeClazzDefinition(KeyEventEntry entry) {
        append(entry.getClassName());
        if (entry.hasRemark()) {
            append(SPACE).append(entry.getRemark());
        }
        append(SPACE).append(entry.getJavadoc()).append(NEWLINE);
        return this;
    }

    private PlainTextRenderer append(String s) {
        content.append(s);
        return this;
    }

}
