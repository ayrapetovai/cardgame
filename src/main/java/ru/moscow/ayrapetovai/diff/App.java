package ru.moscow.ayrapetovai.diff;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.util.Set;

/**
 * Created by artem on 30.01.17.
 */
public class App {
    public static void main(String[] args) {
        File sourceA = new File("./src/test/resources/A.java");
        String sourceFileA = Helper.readSourceFile(sourceA);
        CompilationUnit compilationUnitA = JavaParser.parse(sourceFileA);

        File sourceB = new File("./src/test/resources/B.java");
        String sourceFileB = Helper.readSourceFile(sourceB);
        CompilationUnit compilationUnitB = JavaParser.parse(sourceFileB);

        DiffExtractor ex = new DiffExtractor(() -> new DifferenceVisitor());
        Set<Difference> diffs = ex.extractDifference(compilationUnitA, compilationUnitB);
        System.out.println();
        System.out.println("diffs:");
        diffs.stream().forEach( d -> {
            System.out.println(d);
        });
    }
}
