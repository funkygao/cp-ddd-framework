package ddd.plus.showcase.reverse;

import io.github.dddplus.ast.DomainModelAnalyzer;
import io.github.dddplus.ast.DomainModelAnalyzerTest;
import io.github.dddplus.ast.FileWalker;
import io.github.dddplus.ast.ReverseEngineeringModel;
import io.github.dddplus.ast.view.PlainTextBuilder;
import io.github.dddplus.ast.view.PlantUmlBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class WmsReverseModelingTest {
    private final TargetSource targetSource = new TargetSource();
    private final File root = DomainModelAnalyzerTest.moduleRoot("dddplus-test");

    @Test
    @Disabled // 手工按需可视化，无需集成到CI flow
    void visualizeTheReverseModel() throws IOException {
        ReverseEngineeringModel model = new DomainModelAnalyzer()
                .scan(root)
                .analyze(targetSource);
        new PlantUmlBuilder()
                .direction(PlantUmlBuilder.Direction.TopToBottom)
                .skinParamPolyline()
                .build(model)
                .renderSvg("../doc/wms.svg");
    }

    @Test // integrated CI flow and auto generate pull request: reviewer check the diff
    void generateForwardModel() throws IOException {
        ReverseEngineeringModel model = new DomainModelAnalyzer()
                .scan(root)
                .analyze(targetSource);
        new PlainTextBuilder()
                .build(model)
                .render("../doc/wms.txt");
    }

    private static class TargetSource implements FileWalker.Filter {
        @Override
        public boolean interested(int level, String path, File file) {
            // 去掉(单测，基础设施层)
            return path.contains("showcase") && !path.contains("Test") && !path.contains("infra");
        }
    }

}
