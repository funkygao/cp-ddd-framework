package io.github.dddplus.ast.view;

import io.github.dddplus.ast.ReverseEngineeringModel;
import lombok.Builder;
import net.steppschuh.markdowngenerator.table.Table;
import net.steppschuh.markdowngenerator.text.code.CodeBlock;
import net.steppschuh.markdowngenerator.text.heading.Heading;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

@Builder
public class MarkdownRender {
    private String title;
    private int height = 100;

    public void render(ReverseEngineeringModel model, String pngFileName) throws IOException {
        StringBuilder sb = new StringBuilder();

        Table.Builder tb = new Table.Builder()
                .withAlignments(Table.ALIGN_LEFT, Table.ALIGN_CENTER, Table.ALIGN_RIGHT)
                .addRow("a", "b", "c")
                .addRow("1", "2", "3");

        Heading h1 = new Heading("a", 1);
        sb.append(h1).append("\n");
        sb.append(tb.build()).append("\n");
        sb.append(new CodeBlock("asdf", "java"));
    }

    public void write(String markdown, String fileName) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(fileName);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(osw)) {
            for (String line : markdown.split("\n")) {
                bw.write(line + "\n");
            }
        }
    }
}
