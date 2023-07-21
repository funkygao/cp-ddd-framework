package io.github.dddplus.ast;

import io.github.dddplus.ast.algorithm.JaccardModelSimilarity;
import io.github.dddplus.ast.model.*;
import io.github.dddplus.ast.report.EncapsulationReport;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Domain model analyzer, generating {@link ReverseEngineeringModel}.
 */
@Slf4j
public class DomainModelAnalyzer {
    private File[] dirs;
    private double similarityThreshold = 25; // 25%
    private Set<String> ignoredAnnotations = new HashSet<>(); // in simpleName
    private boolean rawSimilarity = false;
    private boolean debugMode = false;

    public DomainModelAnalyzer scan(File... dirs) {
        this.dirs = dirs;
        return this;
    }

    public DomainModelAnalyzer rawSimilarity() {
        this.rawSimilarity = true;
        return this;
    }

    public DomainModelAnalyzer debug() {
        this.debugMode = true;
        return this;
    }

    /**
     * DSL被用在哪些注解上不处理.
     *
     * @param annotationSimpleNames in simpleName
     */
    public DomainModelAnalyzer ignoreAnnotated(String... annotationSimpleNames) {
        for (String annotation : annotationSimpleNames) {
            ignoredAnnotations.add(annotation);
        }
        return this;
    }

    public DomainModelAnalyzer similarityThreshold(double threshold) {
        this.similarityThreshold = threshold;
        return this;
    }

    public ReverseEngineeringModel analyze() {
        return analyze(null);
    }

    public EncapsulationReport analyzeEncapsulation(FileWalker.Filter filter) {
        FileWalker.Filter actualFilter = new ActualFilter(filter);
        EncapsulationReport report = new EncapsulationReport();
        for (File dir : dirs) {
            new FileWalker(actualFilter, (level, path, file) -> {
                new PublicMethodAstNodeVisitor().visit(FileWalker.silentParse(file), report);
            }).walkFrom(dir);
        }
        return report;
    }

    public ReverseEngineeringModel analyze(FileWalker.Filter filter) {
        ReverseEngineeringModel model = new ReverseEngineeringModel();
        FileWalker.Filter actualFilter = new ActualFilter(filter);
        for (File dir : dirs) {
            log.debug("enter dir: {}", dir.getAbsolutePath());

            // class method distribution
            log.debug("parsing {}", ClassMethodDistributionAstNodeVisitor.class.getSimpleName());
            new FileWalker(actualFilter, (level, path, file) -> {
                new ClassMethodDistributionAstNodeVisitor().visit(FileWalker.silentParse(file), model.getClassMethodReport());
            }).walkFrom(dir);

            // aggregate
            log.debug("parsing {}", AggregateAstNodeVisitor.class.getSimpleName());
            new FileWalker(actualFilter, (level, path, file) -> {
                new AggregateAstNodeVisitor().visit(FileWalker.silentParse(file), model.getAggregateReport());
            }).walkFrom(dir);

            // key element
            log.debug("parsing {}", KeyElementAstNodeVisitor.class.getSimpleName());
            new FileWalker(actualFilter, (level, path, file) -> {
                new KeyElementAstNodeVisitor(rawSimilarity, ignoredAnnotations).visit(FileWalker.silentParse(file), model.getKeyModelReport());
            }).walkFrom(dir);

            // key behavior
            log.debug("parsing {}", KeyBehaviorAstNodeVisitor.class.getSimpleName());
            new FileWalker(actualFilter, (level, path, file) -> {
                new KeyBehaviorAstNodeVisitor(ignoredAnnotations).visit(FileWalker.silentParse(file), model.getKeyBehaviorReport());
            }).walkFrom(dir);

            // key flow
            log.debug("parsing {}", KeyFlowAstNodeVisitor.class.getSimpleName());
            new FileWalker(actualFilter, (level, path, file) -> {
                new KeyFlowAstNodeVisitor(ignoredAnnotations).visit(FileWalker.silentParse(file), model.getKeyFlowReport());
            }).walkFrom(dir);

            // key rule
            log.debug("parsing {}", KeyRuleAstNodeVisitor.class.getSimpleName());
            new FileWalker(actualFilter, (level, path, file) -> {
                new KeyRuleAstNodeVisitor(ignoredAnnotations).visit(FileWalker.silentParse(file), model.getKeyRuleReport());
            }).walkFrom(dir);

            // key usecase
            log.debug("parsing {}", KeyUsecaseAstNodeVisitor.class.getSimpleName());
            new FileWalker(actualFilter, (level, path, file) -> {
                new KeyUsecaseAstNodeVisitor(ignoredAnnotations).visit(FileWalker.silentParse(file), model.getKeyUsecaseReport());
            }).walkFrom(dir);

            // key event
            log.debug("parsing {}", KeyEventAstNodeVisitor.class.getSimpleName());
            new FileWalker(actualFilter, (level, path, file) -> {
                new KeyEventAstNodeVisitor().visit(FileWalker.silentParse(file), model.getKeyEventReport());
            }).walkFrom(dir);

            // key relation
            log.debug("parsing {}", KeyRelationAstNodeVisitor.class.getSimpleName());
            new FileWalker(actualFilter, (level, path, file) -> {
                new KeyRelationAstNodeVisitor().visit(FileWalker.silentParse(file), model.getKeyRelationReport());
            }).walkFrom(dir);
        }

        // similarity
        log.debug("calculating key models similarity");
        JaccardModelSimilarity similarityAnalyzer = new JaccardModelSimilarity();
        List<KeyModelEntry> keyModelEntries = new ArrayList<>(model.getKeyModelReport().getData().values());
        for (int i = 0; i < keyModelEntries.size(); i++) {
            log.debug("fan out similarity of {}", keyModelEntries.get(i).getClassName());
            for (int j = i + 1; j < keyModelEntries.size(); j++) {
                KeyModelEntry model1 = keyModelEntries.get(i);
                KeyModelEntry model2 = keyModelEntries.get(j);
                double similarity = similarityAnalyzer.similarity(model1, model2);
                if (similarity < similarityThreshold) {
                    continue;
                }

                SimilarityEntry entry = SimilarityEntry.builder()
                        .leftClass(model1.getClassName())
                        .rightClass(model2.getClassName())
                        .similarity(similarity)
                        .build();
                model.addSimilarityEntry(entry);
            }
        }

        if (rawSimilarity) {
            log.debug("calculating raw models similarity");
            List<KeyModelEntry> rawModels = new ArrayList<>(model.getKeyModelReport().getRawModels().values());
            for (int i = 0; i < rawModels.size(); i++) {
                log.debug("fan out similarity of raw model {}", rawModels.get(i).getClassName());
                for (int j = i + 1; j < rawModels.size(); j++) {
                    KeyModelEntry model1 = rawModels.get(i);
                    KeyModelEntry model2 = rawModels.get(j);
                    double similarity = similarityAnalyzer.rawModelSimilarity(model1, model2);
                    if (similarity < similarityThreshold) {
                        continue;
                    }

                    SimilarityEntry entry = SimilarityEntry.builder()
                            .leftClass(model1.getClassName())
                            .rightClass(model2.getClassName())
                            .similarity(similarity)
                            .build();
                    model.addRawSimilarityEntry(entry);
                }
            }
        }

        log.debug("building association: aggregate <-> key model");
        for (AggregateEntry aggregateEntry : model.getAggregateReport().getAggregateEntries()) {
            for (KeyModelEntry keyModelEntry : model.getKeyModelReport().keyModelsOfPackage(aggregateEntry.getPackageName())) {
                aggregateEntry.addKeyModelEntry(keyModelEntry);
            }
        }

        log.debug("building association: key model <-> key behavior");
        for (String actor : model.getKeyModelReport().actors()) {
            List<KeyBehaviorEntry> behaviorEntries = model.getKeyBehaviorReport().actorKeyBehaviors(actor);
            model.getKeyModelReport().keyModelEntryOfActor(actor).addKeyBehaviorEntries(behaviorEntries);
        }

        log.debug("building association: aggregate <-> behavior only model");
        for (String actor : model.getKeyBehaviorReport().actors()) {
            if (!model.getKeyModelReport().containsActor(actor)) {
                List<KeyBehaviorEntry> behaviorEntries = model.getKeyBehaviorReport().actorKeyBehaviors(actor);
                if (!behaviorEntries.isEmpty()) {
                    // behavior only model
                    KeyModelEntry keyModelEntry = model.getKeyModelReport().getOrCreateKeyModelEntryForActor(actor);
                    keyModelEntry.setPackageName(behaviorEntries.get(0).getPackageName());
                    keyModelEntry.addKeyBehaviorEntries(behaviorEntries);

                    // register to aggregate
                    AggregateEntry aggregateEntry = model.getAggregateReport().aggregateEntryOfPackage(keyModelEntry.getPackageName());
                    if (aggregateEntry != null) {
                        aggregateEntry.addKeyModelEntry(keyModelEntry);
                    } else {
                        // annotated with KeyFlow only, e,g. service
                    }
                }
            }
        }

        log.debug("building association: key model <-> key rule");
        for (String actor : model.getKeyModelReport().actors()) {
            List<KeyRuleEntry> ruleEntries = model.getKeyRuleReport().keyRulesOfClass(actor);
            model.getKeyModelReport().keyModelEntryOfActor(actor).addKeyRuleEntries(ruleEntries);
        }

        log.debug("building association: key model <-> key flow");
        for (String actor : model.getKeyModelReport().actors()) {
            List<KeyFlowEntry> flowEntries = model.getKeyFlowReport().keyFlowEntriesOfActor(actor);
            model.getKeyModelReport().keyModelEntryOfActor(actor).addKeyFlowEntries(flowEntries);
        }

        log.debug("locate orphan key events");
        for (KeyEventEntry entry : model.getKeyEventReport().getEvents()) {
            if (!model.hasProducer(entry)) {
                entry.setOrphan(true);
            }
        }

        // call graph
        CallGraphAstNodeVisitor callGraphAstNodeVisitor = new CallGraphAstNodeVisitor(dirs);
        for (File dir : dirs) {
            log.debug("parsing {}", CallGraphAstNodeVisitor.class.getSimpleName());
            new FileWalker(actualFilter, (level, path, file) -> {
                callGraphAstNodeVisitor.visit(FileWalker.silentParse(file), model.getCallGraphReport());
            }).walkFrom(dir);
        }

        return model;
    }

    static class ActualFilter implements FileWalker.Filter {
        private final FileWalker.Filter filter;

        ActualFilter(FileWalker.Filter filter) {
            this.filter = filter;
        }

        @Override
        public boolean interested(int level, String path, File file) {
            boolean interested = !path.contains("/target/") && path.endsWith(".java");
            interested = interested && !path.endsWith("Test.java");
            if (filter != null) {
                interested = interested && filter.interested(level, path, file);
            }
            return interested;
        }
    }
}
