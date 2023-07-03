package ddd.plus.showcase.wms.reverse;

import io.github.dddplus.ast.DomainModelAnalyzer;
import io.github.dddplus.ast.DomainModelAnalyzerTest;
import io.github.dddplus.ast.ReverseEngineeringModel;
import io.github.dddplus.ast.view.PlantUmlBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class ModelingTest {

    @Test
    void visualizeTheReverseModel() throws IOException {
        ReverseEngineeringModel model = new DomainModelAnalyzer()
                .scan(DomainModelAnalyzerTest.moduleRoot("dddplus-test"))
                .analyze((level, path, file) -> path.contains("showcase") && !path.contains("Test"));
        new PlantUmlBuilder()
                .skipParamHandWrittenStyle()
                .skinParamPolyline()
                .build(model).renderSvg("../wms.svg");
    }
}
