package ru.moscow.ayrapetovai.diff;

import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.*;
import com.github.javaparser.ast.visitor.EqualsVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.Visitable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by artem on 01.02.17.
 */
public class DifferenceVisitor implements GenericVisitor<Boolean, Visitable> {
    private Set<Difference> differences = new HashSet<>();

    public Set<Difference> getDifferences() {
        return differences;
    }

    public void print(String msg, List<Node> list, int from, int to) {
        System.out.print(msg);
        for (int u = from; u < to; u++)
//                    System.out.println(nodeName(b.get(u)));
            System.out.println(list.get(u));
    }

    public boolean diffChildLists(Node base, Node changed) {
        List<Node> a = base.getChildNodes();
        List<Node> b = changed.getChildNodes();
        int i = 0;
        int j = 0;
        while (true) {
            String sa = a.toString();
            String sb = b.toString();
            if (i == a.size() && j == b.size()) {
                break;
            } else if (a.size() <= i || b.size() <= j) {
                print("changed: ", a, i, a.size());
                print("to:      ", b, j, b.size());
                if (a.size() <= i) {
                    // created
                    for (int q = j; q < b.size(); q++)
                        differences.add(new Difference(nodeName(base) + "/" + a.size(), nodeName(b.get(q))));
                }
                if (b.size() <= j) {
                    // deleted
                    for (int q = i; q < a.size(); q++)
                        differences.add(new Difference(nodeName(a.get(q)), "x"));
                }
                break;
            } else if (diff(a.get(i), b.get(j))) {
                i++; j++;
            } else {
                int z = i;
                int k = j;
                minimaldiff:
                for (; z < a.size(); z++) {
                    k = j;
                    for (;k < b.size(); k++) {
                        if (diff(a.get(z), b.get(k))) {
                            break minimaldiff;
                        }
                    }
                }
                print("changed: ", a, i, z);
                print("to:      ", b, j, k);
                i = z;
                j = k;
            }
        }
        return true;
    }

    public String nodeName(Node node) {
        StringBuilder sb = new StringBuilder();
        for (Node i = node; i.getParentNode().isPresent(); i = i.getParentNode().get()) {
            Node parent = i.getParentNode().get();
            int index = parent.getChildNodes().indexOf(i);
            sb.insert(0, "/" + index);
        }
        return sb.toString();
    }

    public boolean diff(Node a, Node b) {
        if (a.getClass() != b.getClass()) {
            return false;
        }
//        if (a.getChildNodes().size() != b.getChildNodes().size()) {
//            return false;
//        }
        Boolean ret =  a.accept(this, b);
        if (ret == null) {
            ret = EqualsVisitor.equals(a, b);
        }
        return ret;
    }

    @Override
    public Boolean visit(CompilationUnit base, Visitable arg) {
        CompilationUnit changed = (CompilationUnit) arg;
        return diffChildLists(base, changed);
    }

    @Override
    public Boolean visit(PackageDeclaration n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(TypeParameter n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(LineComment n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(BlockComment n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(ClassOrInterfaceDeclaration base, Visitable arg) {
        ClassOrInterfaceDeclaration changed = (ClassOrInterfaceDeclaration) arg;
        List<MethodDeclaration> a = base.getMethods();
        List<MethodDeclaration> b = changed.getMethods();
        int i = 0;
        int j = 0;
        while (true) {
            String sa = a.toString();
            String sb = b.toString();
            if (i == a.size() && j == b.size()) {
                break;
            } else if (a.size() <= i || b.size() <= j) {
//                print("changed: ", a, i, a.size());
//                print("to:      ", b, j, b.size());
                if (a.size() <= i) {
                    // created
                    for (int q = j; q < b.size(); q++)
                        differences.add(new Difference(nodeName(base) + "/" + a.size(), nodeName(b.get(q))));
                }
                if (b.size() <= j) {
                    // deleted
                    for (int q = i; q < a.size(); q++)
                        differences.add(new Difference(nodeName(a.get(q)), "deleted"));
                }
                break;
            } else if (methodSignatureComporator(a.get(i), b.get(j))) {
                i++; j++;
            } else {
                int z = i;
                int k = j;
                minimaldiff:
                for (; z < a.size(); z++) {
                    k = j;
                    for (;k < b.size(); k++) {
                        if (methodSignatureComporator(a.get(z), b.get(k))) {
                            break minimaldiff;
                        }
                    }
                }
//                print("changed: ", a, i, z);
//                print("to:      ", b, j, k);
                i = z;
                j = k;
            }
        }
        return diffChildLists(base, changed);
    }

    private boolean methodSignatureComporator(MethodDeclaration base, MethodDeclaration changed) {
//        int points = 0;
//        points += base.getName().equals(changed.getName())? 1: 0;
//        points += base.getModifiers().equals(changed.getModifiers())? 1: 0;
//        points += base.getParameters().equals(changed.getModifiers())? 10: 0;
        Dessision des = new Dessision()
                .condition(() -> base.getParameters().equals(changed.getModifiers()))
                .condition(() -> base.getName().equals(changed.getName()))
                .condition(() -> base.getModifiers().equals(changed.getModifiers()));
        return des.isTrue();
    }

    @Override
    public Boolean visit(EnumDeclaration n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(EnumConstantDeclaration n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(AnnotationDeclaration n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(AnnotationMemberDeclaration n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(FieldDeclaration n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(VariableDeclarator n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(ConstructorDeclaration n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(MethodDeclaration base, Visitable arg) {
        MethodDeclaration changed = (MethodDeclaration) arg;
        return diffChildLists(base, changed);
    }

    @Override
    public Boolean visit(Parameter base, Visitable arg) {
        Parameter changed = (Parameter) arg;
        return diffChildLists(base, changed);
    }

    @Override
    public Boolean visit(EmptyMemberDeclaration n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(InitializerDeclaration n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(JavadocComment n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(ClassOrInterfaceType n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(PrimitiveType n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(ArrayType n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(ArrayCreationLevel n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(IntersectionType n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(UnionType n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(VoidType n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(WildcardType n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(UnknownType n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(ArrayAccessExpr n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(ArrayCreationExpr n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(ArrayInitializerExpr n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(AssignExpr n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(BinaryExpr n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(CastExpr n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(ClassExpr n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(ConditionalExpr n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(EnclosedExpr n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(FieldAccessExpr base, Visitable arg) {
        FieldAccessExpr changed = (FieldAccessExpr) arg;
        return diffChildLists(base, changed);
    }

    @Override
    public Boolean visit(InstanceOfExpr n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(StringLiteralExpr n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(IntegerLiteralExpr n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(LongLiteralExpr n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(CharLiteralExpr n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(DoubleLiteralExpr n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(BooleanLiteralExpr n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(NullLiteralExpr n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(MethodCallExpr base, Visitable arg) {
        MethodCallExpr changed = (MethodCallExpr) arg;
        return diffChildLists(base, changed);
    }

    @Override
    public Boolean visit(NameExpr base, Visitable arg) {
        NameExpr changed = (NameExpr) arg;
        return diffChildLists(base, changed);
    }

    @Override
    public Boolean visit(ObjectCreationExpr n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(ThisExpr n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(SuperExpr n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(UnaryExpr n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(VariableDeclarationExpr n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(MarkerAnnotationExpr n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(SingleMemberAnnotationExpr n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(NormalAnnotationExpr n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(MemberValuePair n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(ExplicitConstructorInvocationStmt n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(LocalClassDeclarationStmt n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(AssertStmt n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(BlockStmt base, Visitable arg) {
        BlockStmt changed = (BlockStmt) arg;
        return diffChildLists(base, changed);
    }

    @Override
    public Boolean visit(LabeledStmt n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(EmptyStmt n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(ExpressionStmt base, Visitable arg) {
        ExpressionStmt changed = (ExpressionStmt) arg;
        return diffChildLists(base, changed);
    }

    @Override
    public Boolean visit(SwitchStmt n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(SwitchEntryStmt n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(BreakStmt n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(ReturnStmt n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(IfStmt n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(WhileStmt n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(ContinueStmt n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(DoStmt n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(ForeachStmt n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(ForStmt n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(ThrowStmt n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(SynchronizedStmt n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(TryStmt n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(CatchClause n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(LambdaExpr n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(MethodReferenceExpr n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(TypeExpr n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(NodeList n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(Name n, Visitable arg) {
        return null;
    }

    @Override
    public Boolean visit(SimpleName base, Visitable arg) {
        SimpleName changed = (SimpleName) arg;
        if (base.getIdentifier().equals(changed.getIdentifier())) {
            return true;
        } else {
            // changed
            differences.add(new Difference(nodeName(base), nodeName(changed)));
            return false;
        }
    }

    @Override
    public Boolean visit(ImportDeclaration n, Visitable arg) {
        return null;
    }
}
