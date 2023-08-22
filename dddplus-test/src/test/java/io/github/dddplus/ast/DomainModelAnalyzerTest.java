package io.github.dddplus.ast;

import io.github.dddplus.ast.model.AggregateEntry;
import io.github.dddplus.ast.model.KeyModelEntry;
import io.github.dddplus.ast.model.ReverseEngineeringModel;
import io.github.dddplus.ast.view.PlainTextRenderer;
import io.github.dddplus.ast.view.PlantUmlRenderer;
import io.github.dddplus.runtime.registry.IntegrationTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DomainModelAnalyzerTest {

    /**
     * {@link IntegrationTest#exportDomainArtifacts()} will export all extensions info.
     */
    @Test
    void analyze() {
        DomainModelAnalyzer analyzer = new DomainModelAnalyzer();
        analyzer.scan(moduleRoot("dddplus-test"));
        ReverseEngineeringModel model = analyzer.analyze((level, path, file) -> path.contains("design"));
        assertNotNull(model.getAggregateReport());
        assertTrue(model.getKeyRelationReport().size() > 1);
        assertTrue(model.aggregates().size() > 0);
        List<KeyModelEntry> keyModelEntryList = model.getKeyModelReport().keyModelsOfPackage(model.getAggregateReport().getAggregateEntries().get(0).getPackageName());
        assertEquals(4, keyModelEntryList.size());
        assertEquals("io.github.design", keyModelEntryList.get(0).getPackageName());
        AggregateEntry firstAggregate = model.getAggregateReport().get(0);
        assertEquals(firstAggregate.getName(), "foo");
    }

    @Test
    void renderText() throws IOException {
        DomainModelAnalyzer analyzer = new DomainModelAnalyzer();
        analyzer.rawSimilarity()
                .similarityThreshold(60)
                .scan(moduleRoot("dddplus-test"));
        ReverseEngineeringModel model = analyzer.analyze((level, path, file) -> path.contains("design"));
        new PlainTextRenderer()
                .showRawSimilarities()
                .withModel(model)
                .targetFilename("../doc/model.txt")
                .render();
    }

    @Test
    @Disabled
    void renderUml() throws IOException {
        ReverseEngineeringModel model = new DomainModelAnalyzer()
                .rawSimilarity()
                .scan(moduleRoot("dddplus-test"))
                .analyze((level, path, file) -> path.contains("design"));
        new PlantUmlRenderer()
                .appendNote("abc")
                .appendNote("dc")
                .skipParamHandWrittenStyle()
                .skinParamPolyline()
                .classDiagramSvgFilename("../test.svg")
                .withModel(model)
                .render();
    }

    public static File moduleRoot(String module) {
        try {
            return (projectRoot().listFiles(f -> f.getName().equals(module)))[0];
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    private static File projectRoot() throws IOException {
        File currentDir = new ClassPathResource("").getFile(); // dddplus-test/target/test-classes
        return currentDir
                .getParentFile()
                .getParentFile()
                .getParentFile();
    }

}
