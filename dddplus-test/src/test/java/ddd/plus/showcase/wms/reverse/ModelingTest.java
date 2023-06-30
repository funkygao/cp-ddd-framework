package ddd.plus.showcase.wms.reverse;

import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

class ModelingTest {

    void visualizeTheReverseModel() throws IOException {
        ReverseEngineeringModel model = new DomainModelAnalyzer()
                .scan(moduleRoot("dddplus-test"))
                .analyze((level, path, file) -> path.contains("showcase") && !path.contains("Test"));
        new PlantUmlBuilder()
                .skipParamHandWrittenStyle()
                .skinParamPolyline()
                .build(model).renderSvg("../wms.svg");
    }

    static File moduleRoot(String module) throws IOException {
        return (projectRoot().listFiles(f -> f.getName().equals(module)))[0];
    }

    private static File projectRoot() throws IOException {
        File currentDir = new ClassPathResource("").getFile();
        return currentDir
                .getParentFile()
                .getParentFile()
                .getParentFile();
    }
}
