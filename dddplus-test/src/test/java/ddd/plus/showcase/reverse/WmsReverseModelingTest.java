package ddd.plus.showcase.reverse;

import io.github.dddplus.ast.*;
import io.github.dddplus.ast.report.EncapsulationReport;
import io.github.dddplus.ast.view.CallGraphRenderer;
import io.github.dddplus.ast.view.PlainTextRenderer;
import io.github.dddplus.ast.view.PlantUmlRenderer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class WmsReverseModelingTest {
    private final DomainLayerFilter domainLayerFilter = new DomainLayerFilter();
    private final InfrastructureLayerFilter infrastructureLayerFilter = new InfrastructureLayerFilter();
    private final ShowcaseFilter showcaseFilter = new ShowcaseFilter();
    private final File root = DomainModelAnalyzerTest.moduleRoot("dddplus-test");

    @Test
    @Disabled // 手工按需可视化，无需集成到CI flow
    void visualizeDomainModel() throws IOException {
        ReverseEngineeringModel model = new DomainModelAnalyzer()
                .scan(root)
                .analyze(domainLayerFilter);
        new PlantUmlRenderer()
                .direction(PlantUmlRenderer.Direction.TopToBottom)
                .skinParamPolyline()
                .build(model)
                .classDiagramSvgFilename("../doc/wms.svg")
                .render();
        new CallGraphRenderer()
                .targetDotFilename("../doc/callgraph.dot")
                .build(model)
                .render();
    }

    @Test
    @Disabled
    void visualizeImplementation() throws IOException {
        ReverseEngineeringModel model = new DomainModelAnalyzer()
                .scan(root)
                .analyze(infrastructureLayerFilter);
        new PlantUmlRenderer()
                .title("技术实现细节指引")
                .direction(PlantUmlRenderer.Direction.TopToBottom)
                .disableCoverage()
                .skinParamPolyline()
                .build(model)
                .classDiagramSvgFilename("../doc/tech.svg")
                .render();
    }

    @Test // integrated CI flow and auto generate pull request: reviewer check the diff
    void generateForwardModel() throws IOException {
        ReverseEngineeringModel model = new DomainModelAnalyzer()
                .scan(root)
                .rawSimilarity()
                .similarityThreshold(55)
                .analyze(domainLayerFilter);
        new PlainTextRenderer()
                .showRawSimilarities()
                .clustering()
                .targetFilename("../doc/wms.txt")
                .build(model)
                .render();
    }

    @Test
    void generateEncapsulationReport() throws IOException {
        EncapsulationReport report = new DomainModelAnalyzer()
                .scan(root)
                .analyzeEncapsulation(domainLayerFilter);
        report.dump(new File("../doc/encapsulation.txt"));
    }

    @Test
    void enforceAccessors() {
        new AllowedAccessorsEnforcer()
                .scan(root)
                .enforce(showcaseFilter);
    }

    private static class DomainLayerFilter implements FileWalker.Filter {
        @Override
        public boolean interested(int level, String path, File file) {
            // 去掉(单测，基础设施层)
            return path.contains("showcase") && path.contains("wms") && !path.contains("Test") && !path.contains("infra");
        }
    }

    private static class InfrastructureLayerFilter implements FileWalker.Filter {
        @Override
        public boolean interested(int level, String path, File file) {
            // 去掉(单测，领域层)，只保留基础设施层
            return path.contains("showcase") && path.contains("wms") && !path.contains("Test") && path.contains("infra");
        }
    }

    private static class ShowcaseFilter implements FileWalker.Filter {
        @Override
        public boolean interested(int level, String path, File file) {
            return path.contains("showcase") && !path.contains("Test");
        }
    }

}
