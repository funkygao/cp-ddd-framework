package io.github.dddplus.ast.view;

import io.github.dddplus.ast.ReverseEngineeringModel;
import io.github.dddplus.ast.model.*;
import io.github.dddplus.ast.report.CoverageReport;
import io.github.dddplus.dsl.KeyElement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class PlainTextBuilder {
    private static final String SPACE = " ";
    private static final String TAB = SPACE + SPACE + SPACE;
    private static final String NEWLINE = System.getProperty("line.separator");

    private final StringBuilder content = new StringBuilder();
    private ReverseEngineeringModel model;

    public PlainTextBuilder build(ReverseEngineeringModel model) {
        this.model = model;

        appendCoverage();
        model.aggregates().forEach(a -> addAggregate(a));
        addKeyUsecases();
        addOrphanKeyFlows();
        addKeyEvents();

        return this;
    }

    public void render(String txtFilename) throws IOException {
        File file = new File(txtFilename);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.append(content);
        }
    }

    private PlainTextBuilder appendCoverage() {
        CoverageReport report = model.coverageReport();
        append("标注覆盖率：")
                .append(String.format("Class(%d/%.1f%%)", report.getPublicClazzN(), report.clazzCoverage()))
                .append(SPACE)
                .append(String.format("Method(%d/%.1f%%)", report.getPublicMethodN(), report.methodCoverage()))
                .append(SPACE)
                .append(String.format("Property(%d/%.1f%%)", report.getPropertyN(), report.propertyCoverage()))
                .append(NEWLINE);
        return this;
    }

    private PlainTextBuilder addAggregate(AggregateEntry aggregate) {
        append("<<Aggregate: ").append(aggregate.getName()).append(">>").append(NEWLINE);
        for (KeyModelEntry clazz : aggregate.keyModels()) {
            writeClazzDefinition(clazz, aggregate.isRoot(clazz));
        }
        return this;
    }

    private PlainTextBuilder writeClazzDefinition(KeyModelEntry keyModelEntry, boolean isAggregateRoot) {
        append(keyModelEntry.getClassName()).append(NEWLINE);
        if (!keyModelEntry.types().isEmpty()) {
            append(TAB).append("[属性]").append(NEWLINE);
            for (KeyElement.Type type : keyModelEntry.types()) {
                append(TAB)
                        .append(String.format("%-13s %s", type, keyModelEntry.displayFieldByType(type)))
                        .append(NEWLINE);
            }

            if (!keyModelEntry.undefinedTypes().isEmpty()) {
                append(TAB)
                        .append(String.format("%-13s %s", "-NotLabeled-", keyModelEntry.displayUndefinedTypes()))
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
                            .append(entry.displayArgsWithRules())
                            .append(")")
                            .append(SPACE)
                            .append(entry.getJavadoc())
                            .append(SPACE)
                            .append(entry.displayActualClass());
                    if (entry.produceEvent()) {
                        append(String.format(" -> %s", entry.displayEvents()));
                    }
                    append(NEWLINE);
                }
            }

            append(TAB).append("[聚类]").append(NEWLINE);
            List<List<String>> clusters = keyModelEntry.methodClusters();
            for (int i = 0; i < clusters.size(); i++) {
                if (clusters.get(i).isEmpty()) {
                    continue;
                }
                append(TAB).append(TAB).append(clusters.get(i).toString()).append(NEWLINE);
            }
        }

        return this;
    }

    private PlainTextBuilder writeKeyUsecaseClazzDefinition(String actor) {
        append(actor);
        for (KeyUsecaseEntry entry : model.getKeyUsecaseReport().actorKeyUsecases(actor)) {
            append(TAB);
            if (!entry.displayOut().isEmpty()) {
                append(entry.displayOut()).append(SPACE);
            }

            append(entry.displayNameWithRemark())
                    .append("(")
                    .append(entry.displayIn())
                    .append(")")
                    .append(SPACE)
                    .append(entry.getJavadoc())
                    .append(NEWLINE);
        }
        return this;
    }

    private PlainTextBuilder addKeyUsecases() {
        append("<<交互>>").append(NEWLINE);
        for (String actor : model.getKeyUsecaseReport().getData().keySet()) {
            append(TAB).writeKeyUsecaseClazzDefinition(actor).append(NEWLINE);
        }
        return this;
    }

    private PlainTextBuilder addOrphanKeyFlows() {
        append("<<跨聚合复杂流程>>").append(NEWLINE);
        for (String actor : model.getKeyFlowReport().actors()) {
            writeOrphanFlowClazzDefinition(actor);
        }
        return this;
    }

    private PlainTextBuilder writeOrphanFlowClazzDefinition(String actor) {
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
                    .append(entry.displayArgsWithRules())
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

    private PlainTextBuilder addKeyEvents() {
        content.append("<<领域事件>>").append(NEWLINE);
        for (KeyEventEntry entry : model.getKeyEventReport().getEvents()) {
            append(TAB).writeClazzDefinition(entry);
        }
        return this;
    }

    private PlainTextBuilder writeClazzDefinition(KeyEventEntry entry) {
        append(entry.getClassName());
        if (entry.hasRemark()) {
            append(SPACE).append(entry.getRemark());
        }
        append(SPACE).append(entry.getJavadoc()).append(NEWLINE);
        return this;
    }

    private PlainTextBuilder append(String s) {
        content.append(s);
        return this;
    }

}
