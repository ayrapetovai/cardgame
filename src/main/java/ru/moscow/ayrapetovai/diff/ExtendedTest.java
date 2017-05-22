package ru.moscow.ayrapetovai.diff;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * Created by artem on 05.02.17.
 */
public class ExtendedTest {
    enum Type {
        NONE,
        ALTER,
        DELETE,
        MOVE,
        INSERT
    }
    public static class Difference {
        public final int base;
        public final int chng;
        public final String nodeBase;
        public final String nodeChng;
        public final Type type;

        public Difference(int base, int chng, String nodeBase, String nodeChng, Type type) {
            this.base = base;
            this.chng = chng;
            this.nodeBase = nodeBase;
            this.nodeChng = nodeChng;
            this.type = type;
        }
        @Override
        public String toString() {
            return String.format("%1$-10s", type.toString()) + "(" + nodeBase + ": " + base + ") (" + nodeChng + ": " + chng + ")";
        }
    }

    public static void main(String[] args) {
        BiFunction<String, String, Boolean> relevanter =
//                (s1, s2) -> s1.compareToIgnoreCase(s2) == 0 || s1.contains(s2) || s2.contains(s1);
                (s1, s2) -> s1.compareToIgnoreCase(s2) == 0;

        List<String> a1 = Arrays.asList("A", "B", "C");
        List<String> b1 = Arrays.asList("A", "B", "C");
        diff(a1, b1, relevanter, String::equals);

        List<String> a2 = Arrays.asList("A", "B", "C");
        List<String> b2 = Arrays.asList("A1", "A", "B", "C", "D");
        diff(a2, b2, relevanter, String::equals);

        List<String> a3 = Arrays.asList("A", "B", "C", "D");
        List<String> b3 = Arrays.asList("A", "C", "B", "D");
        diff(a3, b3, relevanter, String::equals);

        List<String> a4 = Arrays.asList("A", "C", "B");
        List<String> b4 = Arrays.asList("b", "A", "C");
        diff(a4, b4, relevanter, String::equals);

        List<String> a5 = Arrays.asList("A", "B", "C", "D");
        List<String> b5 = Arrays.asList("A", "B", "F", "C", "D");
        diff(a5, b5, relevanter, String::equals);

        List<String> a6 = Arrays.asList("A", "B", "C", "D");
        List<String> b6 = Arrays.asList("A", "F", "C", "D");
        diff(a6, b6, relevanter, String::equals);
    }

    public static <T> void diff(List<String> a, List<String> b,
            BiFunction<T, T, Boolean> relevanter, BiFunction<T, T, Boolean> equaler)
    {
        System.out.println("base : " + a);
        System.out.println("chng : " + b);

        List<Difference> differences = new LinkedList<>();

        for (int i = 0; i < a.size(); i++) {
            boolean methodFound = false;
            searchMoved:
            for (int j = 0; j < b.size(); j++) {
                if (relevanter.apply((T)a.get(i), (T)b.get(j))) {
                    if (equaler.apply((T)a.get(i), (T)b.get(j))) {
                        if (i != j) {
                            Difference diff = new Difference(i, j, a.get(i), b.get(j), Type.MOVE);
                            differences.add(diff);
                        }
                    } else {
                        Difference diff = new Difference(i, j, a.get(i), b.get(j), Type.ALTER);
                        differences.add(diff);
                    }
                    methodFound = true;
                    break searchMoved;
                }
            }
            if (!methodFound) {
                Difference diff = new Difference(i, -1, a.get(i), "", Type.DELETE);
                differences.add(diff);
            }
        }
        for (int i = 0; i < b.size(); i++) {
            boolean methodFound = false;
            searchCreated:
            for (int j = 0; j < a.size(); j++) {
                if (relevanter.apply((T)b.get(i), (T)a.get(j))) {
                    methodFound = true;
                    break searchCreated;
                }
            }
            if (!methodFound) {
                Difference diff =  new Difference(i, i, b.get(i), "", Type.INSERT);
                differences.add(diff);
            }
        }

        differences.stream().forEach(d -> System.out.println(d));

        System.out.println("----------------------------------------");
    }
}
