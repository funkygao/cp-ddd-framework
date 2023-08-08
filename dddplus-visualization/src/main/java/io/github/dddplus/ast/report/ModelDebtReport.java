/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.report;

import io.github.dddplus.ast.model.ReverseEngineeringModel;
import io.github.dddplus.ast.model.KeyModelEntry;
import lombok.Data;

/**
 * The gap between (As-Is, To-Be) regarding domain modeling, only covering semantic code rot.
 *
 * <p/>
 * <ol>代码腐化(code rot)相关内容可以用分类树来归纳:
 * <li>静态时/设计态
 *   <ul>
 *       <li>语法(syntax)层：rule/pattern based tools widely accepted
 *           <ul>
 *               <li>Sonar/Checkstyle/lint/FindBugs/schemaspy/IDEA Inspections/JDepend/Facebook infer/Google ErrorProne/ArchUnit/etc</li>
 *               <li>Halstead 复杂度，方法长度，类长度，类数量，类/方法被引用次数，Statement密度分布，注释密度，etc</li>
 *               <li>安全漏洞扫描/dependency risk</li>
 *               <li>微服务间依赖关系：衡量切分的合理性，但尚无开源工具，需要自研，低难度</li>
 *           </ul>
 *       </li>
 *       <li>语义(semantics)层：目前基本靠review等人肉/非技术手段
 *           <ul>DDDplus 逆向建模的主战场
 *               <li>例如：(一词多意，多词一意)如何识别，经测试GPT4也无法准确识别</li>
 *               <li>例如：工具只能判断注释的完备性，却无法判断一致性、准确性、充分性、不必要性</li>
 *           </ul>
 *       </li>
 *   </ul>
 * </li>
 * <li>动态时/运行态
 *   <ul>
 *       <li>系统的可观测性、performance risk/evaluation</li>
 *       <li>unplanned traffic risk</li>
 *       <li>incomplete FEMA design</li>
 *       <li>运行时覆盖率/never executed code blocks</li>
 *   </ul>
 * </li>
 * <li>过程时
 *   <ul>主要依靠git analysis
 *       <li>(code commit, merge conflict) hotspot</li>
 *       <li>file ownership metrics</li>
 *       <li>branch leading time before being merged/commit size</li>
 *   </ul>
 * </li>
 * <li>管理视角
 *   <ul>
 *       <li>线上故障</li>
 *       <li>技术债墙/排期/过程追踪：如何定义和排序技术债是关键</li>
 *       <li>design review质量</li>
 *       <li>code review质量：主要依靠(简单的数量，历史趋势分析，采样人工分析验证)</li>
 *   </ul>
 * </li>
 * </ol>
 * <ol>根据论文检索，被研究次数排名top 10的代码坏味道：
 * <li>Feature envy</li>
 * <li>Long method</li>
 * <li>God class</li>
 * <li>Data class</li>
 * <li>Duplicated code</li>
 * <li>Refused bequest</li>
 * <li>Blob class</li>
 * <li>Shotgun surgery</li>
 * <li>Long parameter list</li>
 * <li>Spaghetti code</li>
 * </ol>
 */
@Data
public class ModelDebtReport {
    private final ReverseEngineeringModel model;

    private int problematicalFields = 0;
    private int problematicalAggregates = 0;
    private int orphanFlows = 0;
    private AggregateDensity aggregateDensity;
    private int rawSimilarModels = 0;

    public ModelDebtReport(ReverseEngineeringModel model) {
        this.model = model;
    }

    public ModelDebtReport build() {
        aggregateDensity = model.getAggregateReport().density();

        for (KeyModelEntry modelEntry : model.getKeyModelReport().getData().values()) {
            orphanFlows += modelEntry.orphanFlows();
            problematicalFields += modelEntry.problematicalPropertiesN();
        }

        rawSimilarModels = model.getRawSimilarities().size();
        return this;
    }

}
