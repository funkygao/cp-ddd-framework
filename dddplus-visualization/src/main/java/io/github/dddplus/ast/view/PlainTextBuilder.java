package io.github.dddplus.ast.view;

import io.github.dddplus.ast.ReverseEngineeringModel;
import io.github.dddplus.ast.model.*;
import io.github.dddplus.dsl.KeyElement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class PlainTextBuilder {
    private static final String SPACE = " ";
    private static final String TAB = SPACE + SPACE;
    private static final String NEWLINE = System.getProperty("line.separator");

    private final StringBuilder content = new StringBuilder();
    private ReverseEngineeringModel model;

    public PlainTextBuilder build(ReverseEngineeringModel model) {
        this.model = model;

        model.aggregates().forEach(a -> addAggregate(a));
        addKeyUsecases();
        addOrphanKeyFlows();
        addKeyRelations();
        addKeyEvents();

        return this;
    }

    public void render(String txtFilename) throws IOException {
        File file = new File(txtFilename);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.append(content);
        }
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
            for (KeyElement.Type type : keyModelEntry.types()) {
                append(TAB).append(TAB)
                        .append(String.format("%15s %s", type, keyModelEntry.displayFieldByType(type)))
                        .append(NEWLINE);
            }

            if (!keyModelEntry.undefinedTypes().isEmpty()) {
                append(TAB).append(TAB)
                        .append(String.format("%15s %s", "Undefined", keyModelEntry.displayUndefinedTypes()))
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
        if (model.getKeyUsecaseReport().getData().isEmpty()) {
            return this;
        }

        append("<<交互>>").append(NEWLINE);
        for (String actor : model.getKeyUsecaseReport().getData().keySet()) {
            append(TAB).writeKeyUsecaseClazzDefinition(actor).append(NEWLINE);
        }
        return this;
    }

    private PlainTextBuilder addOrphanKeyFlows() {
        if (model.getKeyFlowReport().actors().isEmpty()) {
            return this;
        }

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

    private PlainTextBuilder addKeyRelations() {
        return this;
    }

    private PlainTextBuilder addKeyEvents() {
        if (model.getKeyEventReport().isEmpty()) {
            return this;
        }

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
