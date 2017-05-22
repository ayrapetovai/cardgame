package ru.moscow.ayrapetovai.diff;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

/**
 * Created by artem on 05.02.17.
 */
public class AdvansedTest {
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

    public static <T> void diff(List<String> a, List<String> b,
                                BiFunction<T, T, Boolean> relevantEqual, BiFunction<T, T, Boolean> fullyEqual)
    {
        System.out.println("base : " + a);
        System.out.println("chng : " + b);

        // delete
        for (int i = 0; i < a.size(); i++) {
            boolean found = false;
            for (int j = 0; j < b.size(); j++) {
                if (relevantEqual.apply((T)a.get(i), (T)b.get(j))) {
                    found = true; // count founded - collisions
                    break;
                }
            }
            if (!found)
                System.out.println("delete from " + i + " " + a.get(i));
        }

        // alter
        for (int i = 0; i < a.size(); i++) {
            for (int j = 0; j < b.size(); j++) {
                if (relevantEqual.apply((T)a.get(i), (T)b.get(j))) {
                    if (!fullyEqual.apply((T)a.get(i), (T)b.get(j))) {
                        System.out.println("alter the " + i + " to " + j + " " + a.get(i) + " " + b.get(j));
                    } else {
                        break;
                    }
                }
            }
        }

        // move
        for (int i = 0; i < a.size(); i++) {
            for (int j = 0; j < b.size(); j++) {
                if (relevantEqual.apply((T)a.get(i), (T)b.get(j))) {
                    if (i != j) {
                        System.out.println("move from " + i + " to " + j + " " + b.get(j));
                    }
                    break;
                }
            }
        }

        // insert
        int i = 0;
        int j = 0;
        while (j < b.size()) {
            if (i >= a.size()) {
                System.out.println("insert to " + i + " from " + j + " " + b.get(j));
                j++;
            } else if (!relevantEqual.apply((T)a.get(i), (T)b.get(j))) {
                boolean found = false;
                for (int k = i; k < a.size(); k++) {
                    if (relevantEqual.apply((T)b.get(j), (T)a.get(k))) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.println("insert to " + i + " from " + j + " " + b.get(j));
                }
                j++;
            } else {
                i++; j++;
            }
        }

        System.out.println("----------------------------------------");
    }
}
