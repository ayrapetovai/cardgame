package ru.moscow.ayrapetovai.diff.algo;

import java.util.ArrayList;
import java.util.List;

public class MergeContext {
    boolean[] deleted;
    List<Character>[] appended;
    char[] original;

    MergeContext(char[] original) {
        this.original = original;
        deleted = new boolean[original.length + 1];
        deleted[deleted.length - 1] = true;
        appended = new List[original.length + 1];
    }

    public char[] result() {
        List<Character> result = new ArrayList<>();
        for (int i = 0; i < appended.length; i++) {
            if (appended[i] != null)
                result.addAll(appended[i]);
            if (!deleted[i]) {
                result.add(original[i]);
            }
        }
        char[] r = new char[result.size()];
        for (int i = 0; i < result.size(); i++) {
            r[i] = result.get(i);
        }
        return r;
    }
}
