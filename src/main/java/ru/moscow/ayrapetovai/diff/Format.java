package ru.moscow.ayrapetovai.diff;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;

/**
 * Hello world!
 *
 */
public class Format {
    public static void main( String[] args )
    {
//        File source = new File(args[0]);
        File source = new File("src/test/resources/A.java");
//        File source = new File("src/test/java/ru/moscow/ayrapetovai/diff/Ifs.java");
        String sourceFile = Helper.readSourceFile(source);
        CompilationUnit compilationUnit = JavaParser.parse(sourceFile);
        SuperPrettyPrinterConfiguration conf = new SuperPrettyPrinterConfiguration();
        //conf.setNativePrint(true);
        conf.setVisitorFactory(configuratin -> new SuperVisitor((SuperPrettyPrinterConfiguration) configuratin));
        String code = compilationUnit.toString(conf);
        System.out.println(code);
    }
}
