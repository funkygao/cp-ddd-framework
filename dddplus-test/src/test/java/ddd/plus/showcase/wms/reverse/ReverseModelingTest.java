package ddd.plus.showcase.wms.reverse;

import io.github.dddplus.ast.DomainModelAnalyzer;
import io.github.dddplus.ast.DomainModelAnalyzerTest;
import io.github.dddplus.ast.ReverseEngineeringModel;
import io.github.dddplus.ast.view.PlantUmlBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class ReverseModelingTest {

    @Test
    @Disabled
    void visualizeTheReverseModel() throws IOException {
        ReverseEngineeringModel model = new DomainModelAnalyzer()
                .scan(DomainModelAnalyzerTest.moduleRoot("dddplus-test"))
                .analyze((level, path, file) -> path.contains("showcase") && !path.contains("Test"));
        new PlantUmlBuilder()
                .direction(PlantUmlBuilder.Direction.TopToBottom)
                .skinParamPolyline()
                .build(model).renderSvg("../doc/wms.svg");
    }
}
