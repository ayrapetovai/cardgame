package ru.moscow.ayrapetovai.diff;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.GenericVisitor;

import java.util.Set;
import java.util.function.Supplier;

/**
 * Created by artem on 01.02.17.
 */
public class DiffExtractor {
    private final Supplier<GenericVisitor> visitorFactory;

    public DiffExtractor(Supplier<GenericVisitor> visitorFactory) {
        this.visitorFactory = visitorFactory;
    }

    public Set<Difference> extractDifference(CompilationUnit base, CompilationUnit changed) {
        GenericVisitor visitor = visitorFactory.get();
        visitor.visit(base, changed);
        return ((DifferenceVisitor) visitor).getDifferences();
    }
}
