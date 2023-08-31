package io.github.dddplus.ast.view;

interface IRenderer {
    String SPACE = " ";
    String TAB = SPACE + SPACE + SPACE;
    String NEWLINE = System.getProperty("line.separator");
}
