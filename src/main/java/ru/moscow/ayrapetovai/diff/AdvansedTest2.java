package ru.moscow.ayrapetovai.diff;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * Created by artem on 05.02.17.
 */
public class AdvansedTest2 {
    public static void main(String[] args) {
        BiFunction<String, String, Boolean> relevanter =
//                (s1, s2) -> s1.compareToIgnoreCase(s2) == 0 || s1.contains(s2) || s2.contains(s1);
                (s1, s2) -> s1.compareToIgnoreCase(s2) == 0;

        List<String> a1 = Arrays.asList("A", "B", "C");
        List<String> b1 = Arrays.asList("A", "B", "C");
        diff(a1, b1, relevanter, String::equals);

        List<String> a2 = Arrays.asList("A", "B", "C");
        List<String> b2 = Arrays.asList("A1", "A2", "A", "B", "C", "D");
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

        List<String> a7 = Arrays.asList("A", "B", "C", "D");
        List<String> b7 = Arrays.asList("B", "C", "D");
        diff(a7, b7, relevanter, String::equals);

        List<String> a8 = Arrays.asList("A", "B", "C", "D");
        List<String> b8 = Arrays.asList("A", "B", "C", "D", "E", "F");
        diff(a8, b8, relevanter, String::equals);

        List<String> a9 = Arrays.asList("A", "B", "C", "D", "E", "F");
        List<String> b9 = Arrays.asList("A", "B", "C", "D");
        diff(a9, b9, relevanter, String::equals);
    }

    public static void merge(List<String> base, int x) {

    }

    public static <T> void diff(List<String> a, List<String> b,
                                BiFunction<T, T, Boolean> relevantEqual, BiFunction<T, T, Boolean> fullyEqual)
    {
        System.out.println("base : " + a);
        System.out.println("chng : " + b);

        Set<String> found = new HashSet<>();
        Set<String> altered = new HashSet<>();

        for (int i = 0; i < a.size(); i++) {
            for (int j = 0; j < b.size(); j++) {
                if (relevantEqual.apply((T)a.get(i), (T)b.get(j))) {
                    if (fullyEqual.apply((T)a.get(i), (T)b.get(j))) {
                        found.add(a.get(i));
                    } else {
                        found.add(a.get(i));
                        altered.add(a.get(i));
                    }
                }
            }
        }

        System.out.println("----------------------------------------");
    }
}
