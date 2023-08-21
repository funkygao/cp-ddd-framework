package ddd.plus.showcase.reverse;

import io.github.dddplus.DDDPlusEnforcer;
import io.github.dddplus.ast.DomainModelAnalyzer;
import io.github.dddplus.ast.DomainModelAnalyzerTest;
import io.github.dddplus.ast.FileWalker;
import io.github.dddplus.ast.enforcer.AllowedAccessorsEnforcer;
import io.github.dddplus.ast.enforcer.ExtensionMethodSignatureEnforcer;
import io.github.dddplus.ast.model.ReverseEngineeringModel;
import io.github.dddplus.ast.view.*;
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
                .disableCallGraph()
                .analyze(domainLayerFilter);
        new PlantUmlRenderer()
                .direction(PlantUmlRenderer.Direction.TopToBottom)
                .skinParamPolyline()
                .withModel(model)
                .plantUmlFilename("../doc/showcase/wms.puml")
                .classDiagramSvgFilename("../doc/showcase/wms.svg")
                .render();
    }

    @Test
    void callGraph() throws IOException {
        ReverseEngineeringModel model = new DomainModelAnalyzer()
                .scan(root)
                .analyze(domainLayerFilter);
        new CallGraphRenderer()
                .targetCallGraphDotFile("../doc/showcase/callgraph.dot")
                .targetPackageCrossRefDotFile("../doc/showcase/pkgref.dot")
                .edgeShowsCallerMethod()
                .splines("polyline")
                .withModel(model)
                .render();
        new ClassHierarchyRenderer()
                .targetDotFile("../doc/showcase/classlayer.dot")
                .withModel(model)
                .ignoreParent("BaseDto")
                .ignoreParent("Serializable")
                .render();
    }

    @Test
    void dumpModel() throws Exception {
        ReverseEngineeringModel model = new DomainModelAnalyzer()
                .scan(root)
                .disableCallGraph()
                .analyze(domainLayerFilter);
        model.dump("../doc/model.db");
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
                .withModel(model)
                .classDiagramSvgFilename("../doc/showcase/tech.svg")
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
                .targetFilename("../doc/showcase/wms.txt")
                .withModel(model)
                .render();
    }

    @Test
    void generateEncapsulationReport() throws IOException {
        ReverseEngineeringModel model = new DomainModelAnalyzer()
                .scan(root)
                .analyzeEncapsulation(domainLayerFilter);
        new EncapsulationRenderer()
                .withModel(model)
                .targetFilename("../doc/showcase/encapsulation.txt")
                .render();
    }

    @Test
    void enforceAccessors() {
        new AllowedAccessorsEnforcer()
                .scan(root)
                .enforce(showcaseFilter);
    }

    @Test
    void enforceExtensionMethodSignature() throws IOException {
        new ExtensionMethodSignatureEnforcer()
                .scan(root)
                .enforce();
    }

    @Test
    void dddplusEnforce() {
        new DDDPlusEnforcer()
                .scanPackages("ddd.plus.showcase.wms")
                .enforce();
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
