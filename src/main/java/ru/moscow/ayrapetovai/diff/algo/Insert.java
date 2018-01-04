package ru.moscow.ayrapetovai.diff.algo;

import java.util.ArrayList;

public class Insert implements Instruction {
    final int into;
    final int from;
    final char what;

    public Insert(int into, int from, char what) {
        this.into = into;
        this.from = from;
        this.what = what;
    }

    @Override
    public void apply(MergeContext mergeContext) {
        if (mergeContext.appended[into] == null) {
            mergeContext.appended[into] = new ArrayList<>();
        }
        mergeContext.appended[into].add(what);
    }

    @Override
    public String toString() {
        return "insert (" + what + ") to " + into;
    }
}
