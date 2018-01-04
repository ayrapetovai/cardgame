package ru.moscow.ayrapetovai.diff.anew;

import ru.moscow.ayrapetovai.diff.algo.*;

import java.util.ArrayList;
import java.util.List;

public class DiffAlgorithm {
    public List<Instruction> diff(char[] a, char[] b) {
        final int m = a.length + 1;
        final int n = b.length + 1;
        boolean[][] equalityMtrx = new boolean[m][n];
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                equalityMtrx[i][j] = a[i-1] == b[j-1];
            }
        }
        int[][] distanceMtrx = new int[m][n];
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (equalityMtrx[i][j])
                    distanceMtrx[i][j] = distanceMtrx[i-1][j-1] + 1;
                else
                    distanceMtrx[i][j] = Math.max(distanceMtrx[i-1][j], distanceMtrx[i][j-1]);
            }
        }

        boolean[][] pathMtrx = new boolean[m][n];
        char[][] instructionMtrx = new char[m][n];
        int k = m - 1;
        int z = n - 1;
        while (true) {
            if (k > 0 && equalityMtrx[k][z]) {
                System.out.print("  " + a[k - 1]);
                pathMtrx[k][z] = true;
                k--;
                z--;
            } else if (z > 0 && (k == 0 || distanceMtrx[k][z-1] >= distanceMtrx[k-1][z])) {
                pathMtrx[k][z] = true;
                System.out.print(" +" + b[z - 1]);
                instructionMtrx[k][z] = 'i';
                z--;
            } else if (k > 0 && (z == 0 || distanceMtrx[k][z-1] < distanceMtrx[k-1][z])) {
                pathMtrx[k][z] = true;
                System.out.print(" -" + a[k - 1]);
                instructionMtrx[k][z] = 'r';
                k--;
            } else {
                System.out.print(" ");
                break;
            }
        }

        System.out.println();
        k = 0;
        z = 0;
        int[][] sourceMtrx = new int[m][n];
        while (k < m && z < n) {
            if (instructionMtrx[k][z] > 0) {
                System.out.print(instructionMtrx[k][z] + " ");
                if (instructionMtrx[k][z] == 'i') {
                    for (int i = 0; i < m; i++) {
                        if (i != k && equalityMtrx[i][z]) {
                            int removealIndex = -1;
                            for (int j = 0; j < n; j++) {
                                if (instructionMtrx[i][j] == 'r') {
                                    removealIndex = j;
                                    break;
                                }
                            }
                            if (removealIndex > -1) {
                                instructionMtrx[k][z] = 'm';
                                instructionMtrx[i][removealIndex] = '.';
                            } else {
                                instructionMtrx[k][z] = 'c';
                            }
                            sourceMtrx[k][z] = i - 1;
                            break;
                        }
                    }
                }
            }
            if (z + 1 < n && pathMtrx[k][z + 1]) {
                z++;
            } else if (k + 1 < m && pathMtrx[k + 1][z]) {
                k++;
            } else {
                z++;
                k++;
            }
        }

        System.out.println();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (pathMtrx[i][j])
                    System.out.print("0");
                else
                    System.out.print("#");
            }
            System.out.println();
        }

        System.out.println();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (instructionMtrx[i][j] > 0) {
                    System.out.print(instructionMtrx[i][j]);
                } else {
                    System.out.print("*");
                }
            }
            System.out.println();
        }

        List<Instruction> instructions = new ArrayList<>();
        k = 0;
        z = 0;
        while (k < m && z < n) {
            if (instructionMtrx[k][z] > 0) {
                System.out.print(instructionMtrx[k][z] + " ");
                Instruction ins = null;
                switch (instructionMtrx[k][z]) {
                    case 'r':
                        ins = new Remove(k-1); break;
                    case 'i':
                        ins = new Insert(k, z-1, b[z - 1]); break;
                    case 'c':
                        ins = new Copy(sourceMtrx[k][z], k); break;
                    case 'm':
                        ins = new Move(sourceMtrx[k][z], k); break;
                }
                instructions.add(ins);
            }
            if (z + 1 < n && pathMtrx[k][z + 1]) {
                z++;
            } else if (k + 1 < m && pathMtrx[k + 1][z]) {
                k++;
            } else {
                z++;
                k++;
            }
        }
        System.out.println();
        return instructions;
    }
}
