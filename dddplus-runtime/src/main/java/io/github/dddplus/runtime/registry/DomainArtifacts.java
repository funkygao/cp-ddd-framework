/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.runtime.registry;

import io.github.dddplus.ext.IDomainExtension;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 对外输出的领域物件，即核心的领域抽象.
 * <p>
 * <p>方便上层集成，例如：构建配置中心，业务可视化平台等.</p>
 * <p>业务抽象的可视化，在构建业务系统时非常重要：平台能力可以透出，需求传递高效.</p>
 */
public class DomainArtifacts {
    private static final DomainArtifacts instance = new DomainArtifacts();

    @Getter
    private List<Domain> domains;

    @Getter
    private Map<String, List<Step>> steps; // key is activityCode

    @Getter
    private List<Extension> extensions;

    /**
     * 获取单例的领域物件.
     *
     * <p>通过领域物件，获取所有的业务元素：步骤，扩展点等</p>
     */
    public static DomainArtifacts getInstance() {
        return instance;
    }

    private DomainArtifacts() {
    }

    synchronized void export() {
        // domains
        this.domains = new ArrayList<>(InternalIndexer.domainDefMap.size());
        domains.addAll(InternalIndexer.domainDefMap.values().stream().map(domainDef -> new Domain(domainDef.getCode(), domainDef.getName())).collect(Collectors.toList()));

        // steps
        this.steps = new HashMap<>();
        for (Map.Entry<String, Map<String, StepDef>> entry : InternalIndexer.domainStepDefMap.entrySet()) {
            final String activity = entry.getKey();
            this.steps.put(activity, new ArrayList<>());
            for (StepDef stepDef : entry.getValue().values()) {
                this.steps.get(activity).add(new Step(activity, stepDef.getCode(), stepDef.getName(), stepDef.getTags()));
            }
        }

        // extensions
        this.extensions = new ArrayList<>();
        // parse indexer pattern extensions
        for (Map.Entry<Class<? extends IDomainExtension>, List<PatternDef>> entry : InternalIndexer.sortedPatternMap.entrySet()) {
            final Extension extension = new Extension(entry.getKey());
            for (PatternDef patternDef : entry.getValue()) {
                extension.getPatterns().add(new Pattern(patternDef.getCode(), patternDef.getName()));
            }
            for (PartnerDef partnerDef : InternalIndexer.partnerDefMap.values()) {
                if (partnerDef.getExtension(extension.ext) != null) {
                    // 该前台实现了该扩展点
                    extension.getPartners().add(new Partner(partnerDef.getCode(), partnerDef.getName()));
                }
            }

            this.extensions.add(extension);
        }
        // parse indexer partner extensions and merge with pattern extensions
        for (PartnerDef partnerDef : InternalIndexer.partnerDefMap.values()) {
            for (Class<? extends IDomainExtension> ext : partnerDef.getExtensionDefMap().keySet()) {
                Extension extensionsOfPattern = null;
                for (Extension extension : this.extensions) {
                    if (extension.ext == ext) {
                        extensionsOfPattern = extension;
                        break;
                    }
                }

                if (extensionsOfPattern == null) {
                    // this extension is implemented only by Partner
                    final Extension extension = new Extension(ext);
                    extension.getPartners().add(new Partner(partnerDef.getCode(), partnerDef.getName()));
                    this.extensions.add(extension);
                } else {
                    // this extension is implemented in both Partner and Pattern
                    // do the merge
                    final Partner partner = new Partner(partnerDef.getCode(), partnerDef.getName());
                    if (!extensionsOfPattern.getPartners().contains(partner)) {
                        extensionsOfPattern.getPartners().add(partner);
                    }
                }
            }
        }
    }

    /**
     * 领域.
     */
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Domain {
        private String code;
        private String name;
    }

    /**
     * 领域步骤.
     */
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Step {
        private String activity;
        private String code;
        private String name;
        private String[] tags;
    }

    /**
     * 扩展点.
     */
    @Getter
    public static class Extension {
        private Class<? extends IDomainExtension> ext;
        private List<Pattern> patterns;
        private List<Partner> partners;

        private Extension(Class<? extends IDomainExtension> ext) {
            this.ext = ext;
            this.patterns = new ArrayList<>();
            this.partners = new ArrayList<>();
        }
    }

    /**
     * 领域模式.
     */
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @EqualsAndHashCode
    public static class Pattern {
        private String code;
        private String name;
    }

    /**
     * 业务前台.
     */
    @Getter
    @AllArgsConstructor(access = AccessLevel.PACKAGE)
    @EqualsAndHashCode
    public static class Partner {
        private String code;
        private String name;
    }

}
