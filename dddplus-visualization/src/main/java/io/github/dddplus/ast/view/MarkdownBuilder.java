package io.github.dddplus.ast.view;

import com.google.common.collect.Sets;
import io.github.dddplus.ast.ReverseEngineeringModel;
import io.github.dddplus.dsl.KeyElement;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.steppschuh.markdowngenerator.table.Table;
import net.steppschuh.markdowngenerator.text.code.CodeBlock;
import net.steppschuh.markdowngenerator.text.heading.Heading;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

public class MarkdownBuilder {
    private final StringBuilder content = new StringBuilder();
    private Set<KeyElement.Type> ignored;

    public MarkdownBuilder build(ReverseEngineeringModel model) {
        return build(model, Sets.newHashSet());
    }

    public MarkdownBuilder build(ReverseEngineeringModel model, Set<KeyElement.Type> ignored) {
        this.ignored = ignored;
        return this;
    }

    private void demo() {
        Table.Builder tb = new Table.Builder()
                .withAlignments(Table.ALIGN_LEFT, Table.ALIGN_CENTER, Table.ALIGN_RIGHT)
                .addRow("a", "b", "c")
                .addRow("1", "2", "3");

        Heading h1 = new Heading("a", 1);
        content.append(h1).append("\n");
        content.append(tb.build()).append("\n");
        content.append(new CodeBlock("asdf", "java"));
    }

    public void render(String mdFileName) throws IOException {
        SourceStringReader reader = new SourceStringReader(content.toString());
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        String desc = reader.generateImage(os, new FileFormatOption(FileFormat.SVG));
        try (OutputStream outputStream = new FileOutputStream(mdFileName)) {
            os.writeTo(outputStream);
            os.close();
        }
    }

}
