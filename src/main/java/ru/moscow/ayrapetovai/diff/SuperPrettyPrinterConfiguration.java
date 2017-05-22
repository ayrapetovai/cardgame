package ru.moscow.ayrapetovai.diff;

import com.github.javaparser.printer.PrettyPrintVisitor;
import com.github.javaparser.printer.PrettyPrinterConfiguration;

import java.util.function.Function;

/**
 * Created by artem on 31.01.17.
 */
public class SuperPrettyPrinterConfiguration extends PrettyPrinterConfiguration {
    private boolean nativePrint;
    private Function<PrettyPrinterConfiguration, PrettyPrintVisitor> visitorFactory;

    public void setNativePrint(boolean nativePrint) {
        this.nativePrint = nativePrint;
    }
    public boolean isNativePrint() {
        return nativePrint;
    }

    @Override
    public Function<PrettyPrinterConfiguration, PrettyPrintVisitor> getVisitorFactory() {
        return visitorFactory;
    }

    @Override
    public SuperPrettyPrinterConfiguration setVisitorFactory(Function<PrettyPrinterConfiguration, PrettyPrintVisitor> visitorFactory) {
        this.visitorFactory = visitorFactory;
        return this;
    }
}
