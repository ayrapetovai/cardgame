package ru.moscow.ayrapetovai.diff;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by artem on 02.02.17.
 */
public class Test {
    private static class Diff {
        int fromA;
        int fromB;
        int toA;
    }
    public static void main(String[] args) {
        List<String> a = new ArrayList<>();
        List<String> b = new ArrayList<>();
        a.add("A"); b.add("A");  // 0
        a.add("B"); b.add("B");  // 1
        a.add("C"); b.add("C1"); // 2
        a.add("D"); b.add("D1"); // 3
        a.add("E"); b.add("E1"); // 4
        a.add("F"); b.add("F");  // 5
        a.add("G"); b.add("G");  // 6
        a.add("H"); b.add("H1"); // 7
        a.add("I"); b.add("I1"); // 8
        a.add("J"); b.add("J");  // 9
        a.add("K"); b.add("K1");  // 10
        a.add("L"); b.add("L");  // 11
        a.add("M"); b.add("M");  // 12
        /*a.add("O");*/ b.add("O");  // 13
        /*a.add("P");*/ b.add("P");  // 14
//        a.add("Q"); b.add("Q");  // 15
//        a.add("R"); b.add("R");  // 16
//        a.add("S"); /*b.add("S");*/  // 17
//        a.add("T"); //b.add("T");  // 18
//        a.add("U"); //b.add("U");  // 19
//        a.add("V"); //b.add("V");  // 20
//        a.add("W"); //b.add("W");  // 21

        int i = 0;
        int j = 0;
        while(true) {
            if (i == a.size() && j == b.size()) {
                break;
            } else if ((a.size() <= i || b.size() <= j)) {
                System.out.println("from " + i + ":" + j);
                System.out.println("to   " + a.size() + ":" + b.size());
                break;
            } else if (a.get(i).equals(b.get(j))) {
                i++; j++;
            } else {
                int z = i;
                int k = j;
                minimaldiff:
                for (; z < a.size(); z++) {
                    k = j;
                    for (;k < b.size(); k++) {
                        if (a.get(z).equals(b.get(k))) {
                            break minimaldiff;
                        }
                    }
                }
                System.out.println("from " + i + ":" + j);
                System.out.println("to   " + z + ":" + k);
                i = z;
                j = k;
            }
        }
    }
}
