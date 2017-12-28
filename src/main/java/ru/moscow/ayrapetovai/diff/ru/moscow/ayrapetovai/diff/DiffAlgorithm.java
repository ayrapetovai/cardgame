package ru.moscow.ayrapetovai.diff.ru.moscow.ayrapetovai.diff;

import java.util.ArrayList;
import java.util.List;



public class DiffAlgorithm {
    private enum State {
        CELL_ANALYSIS, APPEND_REMOVE, COLUMN_SEARCH, STOP, INDEX_ANALYSIS, ROW_SEARCH, SELECT_SHIFT, RIGHT_STEP, APPEND_REMOVE_LAST, MAIN_STATE
    }
    public void diff(char[] a, char[] b) {
        final int m = a.length;
        final int n = b.length;
        boolean[] presenceVector = new boolean[m];
        boolean[][] equalityMatrix = new boolean[m][n];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b.length; j++) {
                if (a[i] == b[j]) {
                    equalityMatrix[i][j] = true;
                    presenceVector[i] = true;
                }
            }
        }

        System.out.print("  ");
        for (char c: b) {
            System.out.print(c + " ");
        }
        System.out.println();

        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + " ");
            for (int j = 0; j < b.length; j++) {
                System.out.print(equalityMatrix[i][j]? "v ": "  ");
            }
            System.out.println();
        }

        State state = State.MAIN_STATE;
        List<String> instructions = new ArrayList<>();
        int i = 0;
        int j = 0;
        int columnFoundIndex = -1;
        while (state != State.STOP) {
            System.out.print(state + " -> ");
            switch (state) {
                case MAIN_STATE:
                    if (presenceVector[i])
                        state = State.CELL_ANALYSIS;
                    else
                        state = State.APPEND_REMOVE;
                    break;
                case APPEND_REMOVE:
                    instructions.add("remove " + i + " (" + a[i] + ")");
                    i++;
                    state = State.MAIN_STATE;
                    break;
                case CELL_ANALYSIS:
                    if (equalityMatrix[i][j]) {
                        i++;
                        j++;
                        state = State.INDEX_ANALYSIS;
                    } else {
                        int horizontal = 0;
                        int verticalIndex = -1;
                        for (int k = 0; k < n; k++) {
                            if (equalityMatrix[i][k]) {
                                horizontal++;
                                verticalIndex = k;
                            }
                        }
                        int vertical = 0;
                        for (int k = 0; k < m; k++) {
                            if (equalityMatrix[k][verticalIndex]) {
                                vertical++;
                            }
                        }
                        if (horizontal < vertical) {
                            instructions.add("remove " + i + " (" + a[i] + ")");
                        }
                        state = State.COLUMN_SEARCH;
                    }
                    break;
                case COLUMN_SEARCH:
                    for (int columnIndex = i + 1; columnIndex < m; columnIndex++) {
                        if (equalityMatrix[columnIndex][j]) {
                            columnFoundIndex = columnIndex;
                            break;
                        }
                    }
                    for (int columnIndex = i - 1; columnIndex >= 0; columnIndex--) {
                        if (equalityMatrix[columnIndex][j]) {
                            columnFoundIndex = columnIndex;
                            break;
                        }
                    }
                    if (columnFoundIndex > 0)
                        state = State.ROW_SEARCH;
                    else
                        state = State.SELECT_SHIFT;
                    break;
                case ROW_SEARCH:
                    boolean found = false;
                    for (int rowIndex = j + 1; rowIndex < n; rowIndex++) {
                        if (equalityMatrix[columnFoundIndex][rowIndex]) {
                            found = true;
                            break;
                        }
                    }
                    for (int rowIndex = j - 1; rowIndex >= 0; rowIndex--) {
                        if (equalityMatrix[columnFoundIndex][rowIndex]) {
                            found = true;
                            break;
                        }
                    }

                    if (found) {
                        instructions.add("copy " + i + " " + columnFoundIndex + " (" + a[columnFoundIndex] + ")");
                    } else {
                        instructions.add("move " + i + " " + columnFoundIndex + " (" + a[columnFoundIndex] + ")");
                    }
                    state = State.RIGHT_STEP;
                    columnFoundIndex = -1;
                    break;
                case SELECT_SHIFT:
                    if (j < n) {
                        instructions.add("insert " + i + " " + j + " (" + b[j] + ")");
                        state = State.RIGHT_STEP;
                    } else if (j >= n) {
                        state = State.APPEND_REMOVE_LAST;
                    }
                    break;
                case APPEND_REMOVE_LAST:
                    for (int k = i; k < m; k++) {
                        instructions.add("remove " + k + " (" + a[k] + ")");
                    }
                    state = State.STOP;
                    break;
                case RIGHT_STEP:
                    if (i + 1 < m && j + 1 < n &&
                            !equalityMatrix[i][j+1] && equalityMatrix[i+1][j+1]) {
                        i++;
                        j++;
                    } else {
                        j++;
                    }
                    state = State.INDEX_ANALYSIS;
                    break;
                case INDEX_ANALYSIS:
                    if (i >= m && j < n) {
                        for (; j < n; j++) {
                            int copyIndex = -1;
                            for (int k = 0; k < m; k++) {
                                if (equalityMatrix[k][j]) {
                                    copyIndex = k;
                                    break;
                                }
                            }
                            if (copyIndex > 0)
                                instructions.add("copy " + i + " " + copyIndex + " (" + a[copyIndex] + ")");
                            else
                                instructions.add("insert " + i + " " + j + " (" + b[j] + ")");
                        }
                        state = State.STOP;
                    } else if (i < m && j >= n) {
                        for (; i < m; i++) {
                            if (!presenceVector[i])
                                instructions.add("remove " + i + " (" + a[i] + ")");
                        }
                        state = State.STOP;
                    } else if (i >= m || j >= n) {
                        state = State.STOP;
                    } else if (i < m && j < n) {
                        state = State.MAIN_STATE;
                    }
                    break;
                case STOP:
                    state = State.STOP;
            }
            System.out.println(state);
        }

        if (instructions.isEmpty()) {
            System.out.println("no instructions");
        } else {
            for (String instruction : instructions) {
                System.out.println(instruction);
            }
        }
    }
}
