package io.github.dddplus.ast;

import io.github.dddplus.ast.model.AggregateEntry;
import io.github.dddplus.ast.model.KeyBehaviorEntry;
import io.github.dddplus.ast.model.KeyModelEntry;
import io.github.dddplus.ast.model.KeyRuleEntry;
import io.github.dddplus.ast.view.PlantUmlBuilder;
import io.github.dddplus.dsl.KeyElement;
import io.github.dddplus.runtime.registry.IntegrationTest;
import io.github.design.CheckTask;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static io.github.dddplus.ast.ReverseModellingTest.moduleRoot;
import static org.junit.jupiter.api.Assertions.*;

class DomainModelAnalyzerTest {

    /**
     * {@link IntegrationTest#exportDomainArtifacts()} will export all extensions info.
     */
    @Test
    void analyze() throws IOException {
        DomainModelAnalyzer analyzer = new DomainModelAnalyzer();
        analyzer.scan(moduleRoot("dddplus-test"));
        ReverseEngineeringModel model = analyzer.analyze((level, path, file) -> path.contains("design"));
        assertNotNull(model.getAggregateReport());
        assertTrue(model.getKeyRelationReport().size() > 1);
        assertTrue(model.aggregates().size() > 0);
        List<KeyModelEntry> keyModelEntryList = model.getKeyModelReport().keyModelsOfPackage(model.getAggregateReport().getAggregateEntries().get(0).getPackageName());
        assertEquals(2, keyModelEntryList.size());
        assertEquals("io.github.design", keyModelEntryList.get(0).getPackageName());
        AggregateEntry firstAggregate = model.getAggregateReport().get(0);
        assertEquals(2, firstAggregate.keyModels().size());
        KeyModelEntry firstKeyModelEntry = firstAggregate.getKeyModelEntries().get(0);
        assertEquals(CheckTask.class.getSimpleName(), firstKeyModelEntry.getClassName());
        List<KeyElement.Type> undefinedTypes = firstKeyModelEntry.undefinedTypes();
        assertTrue(undefinedTypes.contains(KeyElement.Type.DCU));
        assertEquals(2, firstKeyModelEntry.getKeyBehaviorEntries().size());
        KeyBehaviorEntry keyBehaviorEntry = firstKeyModelEntry.getKeyBehaviorEntries().get(0);
        assertEquals(keyBehaviorEntry.getMethodName(), "finish");
        List<KeyRuleEntry> keyRuleEntries = firstKeyModelEntry.getKeyRuleEntries();
        assertTrue(keyRuleEntries.size() > 0);
        assertEquals("isDone", keyRuleEntries.get(0).getMethodName());

        // render
        PlantUmlBuilder pb = new PlantUmlBuilder()
                .header("header")
                .footer("footer")
                .title("title")
                .skinParam("ranksep 150")
                .direction(PlantUmlBuilder.Direction.LeftToRight)
                .build(model);
        String uml = pb.umlContent();
        assertFalse(uml.isEmpty());
    }

    @Test
    @Disabled
    void renderUml() throws IOException {
        DomainModelAnalyzer analyzer = new DomainModelAnalyzer();
        analyzer.scan(moduleRoot("dddplus-test"));
        ReverseEngineeringModel model = analyzer.analyze((level, path, file) -> path.contains("design"));
        PlantUmlBuilder pb = new PlantUmlBuilder();
        pb.build(model).renderSvg("../test.svg");
    }

}
