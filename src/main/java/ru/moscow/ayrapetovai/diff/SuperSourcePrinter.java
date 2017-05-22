package ru.moscow.ayrapetovai.diff;

import static com.github.javaparser.utils.Utils.EOL;

/**
 * Created by artem on 31.01.17.
 */
public class SuperSourcePrinter {
    private final String indentation;
    private int level = 0;
    private boolean indented = false;
    private final StringBuilder buf = new StringBuilder();

    SuperSourcePrinter(final String indentation) {
        this.indentation = indentation;
    }

    SuperSourcePrinter indent() {
        level++;
        return this;
    }

    SuperSourcePrinter unindent() {
        level--;
        return this;
    }

    private void makeIndent() {
        for (int i = 0; i < level; i++) {
            buf.append(indentation);
        }
    }

    public SuperSourcePrinter print(final String arg) {
        if (!indented) {
            makeIndent();
            indented = true;
        }
        buf.append(arg);
        return this;
    }

    public SuperSourcePrinter println(final String arg) {
        print(arg);
        println();
        return this;
    }

    public SuperSourcePrinter println() {
        buf.append(EOL);
        indented = false;
        return this;
    }

    public String getSource() {
        return buf.toString();
    }

    @Override
    public String toString() {
        return getSource();
    }
}
